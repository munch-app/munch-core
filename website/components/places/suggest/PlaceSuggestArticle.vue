<template>
  <div>
    <h2 class="mt-24">Articles</h2>
    <section class="ArticleSection border-3 mt-16 relative">
      <div class="wh-100 flex-center">
        <div class="Padded p-24 flex-column flex-align-center">
          <h5 class="white mt-8 mb-32">Want to edit {{payload.place.name}} articles?</h5>
          <button class="primary-outline" @click="showArticles">Edit Articles</button>
        </div>
      </div>
    </section>

    <portal to="dialog-full" v-if="show.articles">
      <div class="Existing bg-white elevation-1 p-16-24 overflow-y-auto" v-if="payload.articles.length > 0" v-on-clickaway="onClose">
        <h2>Articles</h2>
        <div class="List">
          <div class="Article flex hover-bg-a10 border-3 hover-pointer" v-for="article in payload.articles"
               :key="article.articleId"
               @click="onDialog(article)">
            <div>
              <image-sizes class="Image border-3 overflow-hidden" v-if="article.thumbnail"
                           :sizes="article.thumbnail.sizes">
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

        <no-ssr class="flex-center" style="padding: 24px 0 48px 0">
          <beat-loader color="#084E69" v-if="next.sort" size="14px"
                       v-observe-visibility="{callback: (v) => v && onMore(),throttle:300}"
          />
        </no-ssr>
      </div>
    </portal>

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
          <button class="elevated" @click="onDialogCancel">Cancel</button>
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
      payload: {
        type: Object,
        twoWay: true
      }
    },
    data() {
      return {
        dialog: null,
        loading: false,
        articles: [],
        next: {sort: null},
        more: true,
        selected: false,
        show: {
          articles: false
        }
      }
    },
    methods: {
      onMore() {
        if (this.loading) return
        if (!this.next.sort) return

        this.loading = true
        const params = {size: 20}
        if (this.next.sort) {
          params['next.sort'] = this.next.sort
        }

        return this.$axios.$get(`/api/places/${this.payload.place.placeId}/articles`, {params})
          .then(({data, next}) => {
            this.articles.push(...data)
            this.loading = false
            this.next.sort = next && next.sort || null
            this.more = !!this.next.sort
          })
      },
      onDialog(article) {
        this.dialog = article
      },
      onFlag(article, flag) {
        this.dialog = null
        this.selected = true

        Vue.set(this.payload.removes.articles, article.articleId, {flag, article})
      },
      showArticles() {
        this.show.articles = true
      },
      onDialogCancel() {
        this.dialog = null
        this.selected = true
      },
      onClose() {
        if (this.dialog == null && !this.selected) {
          this.show.articles = false
        }
        this.selected = false
      }
    }
  }
</script>

<style scoped lang="less">
  .Existing {
    .List {
      margin-top: 8px;
      margin-left: -12px;
      margin-right: -12px;
    }

    margin-left: auto;
    margin-right: auto;
    margin-top: 10vh;
    max-height: 100% - 20vh;
    @media (min-width: 768px) {
      width: 600px;
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
  .ArticleSection {
    position: relative;
    background: no-repeat center/cover;

    /*TODO(Joel): Change images*/
    @media (max-width: 992px) {
      background-image: url('~assets/img/search/home_location.0.5.jpg');
    }

    @media (min-width: 992px) {
      background-image: url('~assets/img/search/home_location.jpg');
    }
  }
</style>
