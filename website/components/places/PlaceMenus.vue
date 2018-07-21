<template>
  <div class="PlaceMenus">
    <b-row>
      <b-col cols="4" v-for="data in list" :key="data.key">
        <a :href="data.href" target="_blank" rel="nofollow">
          <div v-if="data.key === 'url'" class="Elevation1 Web Border48">
            <img src="/img/places/web_page.svg">
          </div>
          <image-size v-else class="Elevation1 Image" :size-urls="data.image" background>
            <div class="Container">
              <div v-if="data.source" class="Small SourceButton Border24 Elevation1">{{data.source}}</div>
            </div>
          </image-size>
        </a>
      </b-col>
    </b-row>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";
  import MunchButton from "../core/MunchButton";

  export default {
    name: "PlaceMenus",
    components: {MunchButton, ImageSize},
    props: {
      menus: {
        required: false,
        twoWay: false,
        type: Array
      }
    },
    computed: {
      list() {
        return this.menus.map(function (menu) {
          if (menu.type === 'image') {
            return {
              key: menu.data.sortKey,
              image: menu.data.thumbnail,
              source: menu.data.sourceName,
              href: menu.data.url,
            }
          } else if (menu.type === 'url') {
            return {
              key: 'url',
              href: menu.data,
            }
          }
        }).slice(0, 3)
      }
    }
  }
</script>

<style scoped lang="less">
  .PlaceMenus {
    .Web {
      height: 170px;
      width: 170px;
      display: flex;
      justify-content: center;
      align-content: center;

      img {
        margin: auto;
        width: 70px;
        height: 70px;
      }
    }

    .Image {
      .Container {
        display: flex;
        width: 100%;
        padding-top: 100%;

        justify-content: flex-end;
        align-items: flex-end;
      }

      .SourceButton {
        display: inline;
        background-color: rgba(255, 255, 255, 0.75);
        padding: 6px 10px;
        margin-right: 8px;
        margin-bottom: 8px;
      }
    }
  }
</style>
