<template>
  <div class="container">
    <masonry-wall ref="wall" id="PlaceImageWall" :items="items" @append="append" :min="2"
                  :options="{lanes:{2:{padding: 8}}}">
      <template slot-scope="{item, index}">
        <div class="ImageItem hover-pointer" @click="selected = index">
          <image-size class="border-3" :image="{sizes: item.sizes}" grow="height">
            <div class="ImageContainer">
              <div class="Title" v-if="item.title || item.caption">
                {{item.title || item.caption}}
              </div>

              <div class="Author" v-if="item.instagram">
                <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/feed/instagram.svg')"/>
                <div class="Name">{{item.instagram.username}}</div>
              </div>
              <div class="Author" v-if="item.article">
                <simple-svg class="Icon" fill="white" :filepath="require('~/assets/icon/feed/article.svg')"/>
                <div class="Name">{{item.article.domain.name}}</div>
              </div>
            </div>
          </image-size>
        </div>
      </template>
    </masonry-wall>

    <no-ssr>
      <place-image-wall-dialog
        v-if="selectedItem" :item="selectedItem" @next="selected++" @prev="selected--" @close="selected = -1"
      />
    </no-ssr>

    <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
      <beat-loader color="#084E69" v-if="next.sort" size="14px"/>
    </no-ssr>
  </div>
</template>

<script>
  import MasonryWall from "../core/MasonryWall";
  import ImageSize from "../core/ImageSize";
  import DialogNavigation from "../layouts/DialogNavigation";
  import PlaceImageWallDialog from "./PlaceImageWallDialog";

  export default {
    name: "PlaceImageWall",
    components: {PlaceImageWallDialog, DialogNavigation, ImageSize, MasonryWall},
    props: {
      placeId: {
        required: true,
        type: String
      },
      preload: {
        type: Array,
        required: false
      }
    },
    data() {
      return {
        items: this.preload || [],
        selected: -1,
        loading: false,
        next: {sort: null}
      }
    },
    mounted() {
      if (this.preload && this.preload.length > 0) {
        this.next.sort = this.preload[this.preload.length - 1].sort
      } else {
        // First Load
        this.append(() => {
          this.$refs.wall.fill()
        }, true)
      }
    },
    computed: {
      selectedItem() {
        return this.items[this.selected]
      },
    },
    methods: {
      append(then, force) {
        if (this.loading) return
        if (!force && !this.next.sort) return

        this.loading = true
        const params = {size: 20}
        if (this.next.sort) {
          params['next.sort'] = this.next.sort
        }
        return this.$axios.$get(`/api/places/${this.placeId}/images`, {params})
          .then(({data, next}) => {
            this.items.push(...data)
            this.loading = false
            this.next.sort = next && next.sort || null
          })
      },
    }
  }
</script>

<style scoped lang="less">
  .ImageContainer {
    width: 100%;
    height: 100%;
    padding: 10px;

    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .Title {
      text-align: right;
      margin-left: 4px;
      margin-right: 4px;

      line-height: 24px;
      font-size: 16px;
      font-weight: 600;

      min-height: 24px;
      max-height: 48px;

      color: white;

      -webkit-line-clamp: 2;

      overflow: hidden;
      display: -webkit-box;
      -webkit-box-orient: vertical;
    }

    .Author {
      display: flex;

      .Icon {
        width: 16px;
        height: 16px;
      }

      .Name {
        margin-left: 3px;

        height: 16px;
        line-height: 15px;
        font-size: 13px;
        font-weight: 600;
        color: white;
      }
    }
  }

  .ImageContainer {
    opacity: 0;
    transition: all 0.3s cubic-bezier(.25, .8, .25, 1);

    &:hover {
      opacity: 1;
      background-color: rgba(0, 0, 0, 0.4);
    }
  }
</style>
