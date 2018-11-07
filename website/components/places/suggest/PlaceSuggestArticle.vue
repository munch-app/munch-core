<template>
  <div>
    <p>Own a blog, want it published on munch? <a class="text-underline s700 weight-600"
                                                  href="https://partner.munch.app"
                                                  target="_blank">partner.munch.app</a></p>

    <div class="Existing" v-if="data.articles.length > 0">
      <h2>Articles</h2>

      <div class="List">
        <div class="Article flex hover-bg-a10 border-3 hover-pointer" v-for="article in data.articles"
             :key="article.articleId"
             @click="onDialog(article)">
          <div>
            <image-sizes class="Image border-3 overflow-hidden" v-if="article.thumbnail" :sizes="article.thumbnail.sizes">
              <div class="OverlayA20 relative hover-bg-a60 wh-100 flex-center">
                <simple-svg class="wh-32px" fill="white" :filepath="require('~/assets/icon/place/suggest/flag.svg')"/>
              </div>
            </image-sizes>
            <div v-else class="bg-whisper200 wh-100"></div>
          </div>

          <div class="Content flex-column-justify-between">
            <h5 class="text-ellipsis-2l">{{article.title}}</h5>
            <h6 class="text-ellipsis-1l">by <span class="s700">{{article.domain.name}}</span></h6>
          </div>
        </div>
      </div>
    </div>

    <no-ssr>
      <portal to="dialog-styled" v-if="dialog" class="Dialog">
        <h3>Flag Article</h3>
        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'NotRelated')">
          <div class="text">Not related article</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'NotPlace')">
          <div class="text">Does not belong to place</div>
          <div class="checkbox"/>
        </div>

        <div class="flex-between hover-pointer" @click="onFlag(dialog, 'Explicit')">
          <div class="text">Explicit content</div>
          <div class="checkbox"/>
        </div>

        <div class="right">
          <button class="clear-elevated" @click="dialog = null">Cancel</button>
        </div>
      </portal>
    </no-ssr>
  </div>
</template>

<script>
  import Vue from 'vue'
  import ImageSizes from "../../core/ImageSizes"

  export default {
    name: "PlaceSuggestArticle",
    components: {ImageSizes},
    props: {
      data: Object,
      payload: {
        type: Object,
        twoWay: true
      }
    },
    data() {
      return {
        dialog: null
      }
    },
    methods: {
      onDialog(article) {
        this.dialog = article
        // Vue.set(this.payload, image.imageId, image)
      },
      onFlag(article, flag) {
        this.dialog = null
        Vue.set(this.payload.removes.articles, article.articleId, {flag, article})
      }
    }
  }
</script>

<style scoped lang="less">
  .Existing {
    margin-top: 32px;

    .List {
      margin-top: 8px;
      margin-left: -12px;
      margin-right: -12px;
    }
  }

  .Article {
    padding: 12px 12px;

    .Image {
      width: 100px;
      height: 80px;

      .OverlayA20 {
        background: rgba(0, 0, 0, 0.2);
      }
    }

    .Content {
      margin-left: 20px;
    }
  }

  .Dialog {
    > div {
      padding-top: 8px;
      padding-bottom: 8px;
      margin-bottom: 0;
    }
  }
</style>
