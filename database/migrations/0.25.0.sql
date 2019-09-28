create index uk_my3rcdy96e9fdwfs
	on placeimage (place_id desc, image_uid desc);

alter table placeimage
	add constraint uk_my3rcdy96e9fdwfs
		unique (place_id, image_uid);

-- TODO(fuxing): ProfileSocial
-- TODO(fuxing): ProfileMedia
-- TODO(fuxing): Mention
