package munch.places.menu.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.AccessControl;
import com.munch.utils.file.ContentTypeError;
import com.munch.utils.file.FileMapper;
import com.squareup.pollexor.Thumbor;
import com.squareup.pollexor.ThumborUrlBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 2:35 AM
 * Project: munch-core
 */
@Singleton
public class MenuMapper {

    private final MenuDatabase database;
    private final FileMapper fileMapper;

    private final Thumbor thumbor;
    private final TikaConfig tika;

    @Inject
    public MenuMapper(MenuDatabase database, FileMapper fileMapper, Thumbor thumbor) {
        this.database = database;
        this.fileMapper = fileMapper;
        this.thumbor = thumbor;
        this.tika = TikaConfig.getDefaultConfig();
    }

    // TODO menu mapper

    public void putImage(String placeId, String menuId, InputStream inputStream, long length, String contentType) throws IOException, ContentTypeError {
        Menu menu = new Menu();
        menu.setPlaceId(placeId);
        menu.setMenuId(menuId);
        menu.setCreatedDate(new Date());

        Menu.Image image = new Menu.Image();
        image.setKinds(new HashSet<>());

        // Persist original type
        Menu.ImageType original = new Menu.ImageType();
        original.setKind(ImageKind.Original);
        original.setKey(menuId + getExtension(contentType));
        fileMapper.put(original.getKey(), inputStream, length, contentType, AccessControl.PublicRead);

        // Persist 200x200 type
        Menu.ImageType x200 = new Menu.ImageType();
        x200.setKind(ImageKind.X200);
        x200.setKey(ImageKind.X200.makeKey(menuId, getExtension(contentType)));
        String resizeUrl = thumbor.buildImage(fileMapper.getUrl(original.getKey()))
                .resize(ImageKind.X200.getWidth(), ImageKind.X200.getHeight())
                .fitIn(ThumborUrlBuilder.FitInStyle.FULL).toUrl();
        // Download 200x200 file
        File x200File = File.createTempFile(x200.getKey(), "");
        FileUtils.copyURLToFile(new URL(resizeUrl), x200File);
        fileMapper.put(x200.getKey(), x200File);

        menu.setImage(image);
        image.getKinds().add(original);
        image.getKinds().add(x200);
        database.put(menu);
    }

    public void putPdf(String placeId, String menuId, InputStream inputStream, long length, String contentType) {
        Menu menu = new Menu();
        menu.setPlaceId(placeId);
        menu.setMenuId(menuId);
        menu.setCreatedDate(new Date());

        Menu.Pdf pdf = new Menu.Pdf();
        pdf.setKey(menuId + getExtension(contentType));
        menu.setPdf(pdf);

        fileMapper.put(pdf.getKey(), inputStream, length, contentType, AccessControl.PublicRead);
        database.put(menu);
    }

    public void putWebsite(String placeId, String menuId, String websiteUrl) {
        Menu menu = new Menu();
        menu.setPlaceId(placeId);
        menu.setMenuId(menuId);
        menu.setCreatedDate(new Date());

        Menu.Website website = new Menu.Website();
        website.setUrl(websiteUrl);
        menu.setWebsite(website);
        database.put(menu);
    }

    public void delete(String menuId) {
        Menu menu = database.get(menuId);
        if (menu == null) return;

        if (menu.getPdf() != null) {
            fileMapper.remove(menu.getPdf().getKey());
        } else if (menu.getImage() != null) {
            for (Menu.ImageType type : menu.getImage().getKinds()) {
                fileMapper.remove(type.getKey());
            }
        }
        database.delete(menuId);
    }

    /**
     * @param contentType Content-Type
     * @return to extension; e.g. png/jpg
     */
    private String getExtension(String contentType) {
        try {
            String extension = tika.getMimeRepository().forName(contentType).getExtension();
            if (StringUtils.isNotEmpty(extension)) return extension;

            throw new RuntimeException("Extension cannot be empty.");
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }
}
