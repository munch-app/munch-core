<template>
  <div class="container pt-48 pb-128">
    <div class="w-100">
      <div class="flex-row w-100">
        <div class="PublicationPic flex-no-shrink hover-pointer" @click="state = 'image'">
          <cdn-img v-if="publication.image" :image="publication.image">
            <div class="flex-center bg-overlay">
              <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
            </div>
          </cdn-img>
          <div v-else class="bg-steam flex-center">
            <simple-svg class="wh-48px" fill="#ccc" :filepath="require('~/assets/icon/icons8-camera.svg')"/>
          </div>
        </div>
        <div class="ml-24 w-100">
          <div>
            <input class="Name w-100 h1" v-model="publication.name" placeholder="Name"/>
          </div>

          <div class="mt-16">
            <text-auto class="regular" v-model="publication.description" placeholder="Description"/>
            <div class="tiny mb-4" :class="{error: publication.description && publication.description.length > 250}">
              {{(publication.description || "").length}}/250 (Required)
            </div>
          </div>
        </div>
      </div>

      <div class="mt-24">
        <text-auto class="regular" v-model="publication.body" placeholder="Body"/>
        <div class="tiny mb-4" :class="{error: publication.body && publication.body.length > 800}">
          {{(publication.body || "").length}}/800
        </div>
      </div>

      <div class="mt-24">
        <button @click="save" class="blue">Save</button>
      </div>
    </div>

    <div v-if="publication.id" class="mt-48">
      <div class="flex-align-center">
        <h3>{{publication.name}} Articles</h3>
        <div class="ml-24">
          <button class="tiny blue-outline" @click="state = 'add'">Add</button>
        </div>
      </div>

      <div class="mt-48">
        <div v-for="pa in articles" :key="pa.uid" class="hr-bot ptb-16">
          <h4>{{pa.article.title}}</h4>
          <p class="m-0">{{pa.article.description}}</p>
          <div class="mt-8">
            <button class="tiny outline" @click="onArticleDelete(pa.article)">Remove</button>
          </div>
        </div>

        <div class="flex-center ptb-32" v-if="next">
          <button class="blue-outline" @click="loadArticles">Load more</button>
        </div>
      </div>
    </div>

    <image-upload-dialog v-if="state === 'image'" @on-image="onImage" @on-close="state = null"/>

    <system-article-selection-dialog v-if="state === 'add'" @on-article="onArticlePut" @on-close="state = null"/>
  </div>
</template>

<script>
  import TextAuto from "../../../components/utils/TextAuto";
  import CdnImg from "../../../components/utils/image/CdnImg";
  import ImageUploadDialog from "../../../components/utils/image/ImageUploadDialog";
  import System from "../../../layouts/system";
  import SystemArticleSelectionDialog from "../../../components/system/SystemArticleSelectionDialog";

  export default {
    components: {SystemArticleSelectionDialog, System, ImageUploadDialog, CdnImg, TextAuto},
    layout: 'system',
    data() {
      return {
        state: null,
        articles: [],
        cursor: {},
      }
    },
    asyncData({$api, params: {id}}) {
      if (id === 'new') {
        return {
          publication: {
            id: null,
            name: '',
            description: '',
            body: '',
            tags: []
          },
        }
      }

      return $api.get(`/admin/publications/${id}`)
        .then(({data: publication}) => {
          return {publication}
        })
    },
    computed: {
      next() {
        return this.cursor?.next
      }
    },
    mounted() {
      this.loadArticles()
    },
    methods: {
      onImage(image) {
        this.publication.image = image
        this.state = null
      },
      onArticlePut(article) {
        this.state = null
        return this.$api.post(`/admin/publications/${this.publication.id}/articles/${article.id}`, {})
          .then(() => {
            this.loadArticles()
          })
          .catch((err) => this.$store.dispatch('addError', err))
      },
      onArticleDelete(article) {
        return this.$api.delete(`/admin/publications/${this.publication.id}/articles/${article.id}`)
          .then(() => {
            this.loadArticles()
          })
          .catch((err) => this.$store.dispatch('addError', err))
      },
      save() {
        if (this.publication.id) {
          return this.$api.patch(`/admin/publications/${this.publication.id}`, this.publication)
            .then(({data: publication}) => {
              this.publication = publication
            }).catch(error => {
              return this.$store.dispatch('addError', error)
            })
        }

        return this.$api.post(`/admin/publications`, this.publication)
          .then(({data: publication}) => {
            this.publication = publication
            window.history.replaceState({}, document.title, `/system/publications/${publication.id}`)
          }).catch(error => {
            return this.$store.dispatch('addError', error)
          })
      },
      loadArticles() {
        return this.$api.get(`/admin/publications/${this.publication.id}/articles`, {
          params: {
            size: 30,
            cursor: this.next
          }
        }).then(({data: articles, cursor}) => {
          this.articles.push(...articles)
          console.log(cursor)
          this.cursor = cursor
        })
      },
    }
  }
</script>

<style scoped lang="less">
  .PublicationPic > div {
    width: 160px;
    height: 160px;
    overflow: hidden;
  }

  .Name {
    border: none;

    &:focus {
      outline: none;
      color: rgba(0, 0, 0, 0.75);
    }

    &::placeholder {
      color: rgba(0, 0, 0, 0.6);
    }
  }
</style>
