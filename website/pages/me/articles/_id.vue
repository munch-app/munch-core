<template>
  <div class="container-768 pt-48 pb-128">
    <article-editor v-model="revision" @on-save="onSave"/>

    <header-middle>
      <div class="flex-align-center flex-justify-end relative">
        <div class="mr-8">
          <button @click="onPublishStart" class="tiny pink-outline">Ready to publish?</button>
        </div>
        <div>
          <button @click="state.more = !state.more" class="small">
            <simple-svg class="wh-20px" fill="black" :filepath="require('~/assets/icon/icons8-more.svg')"/>
          </button>
        </div>

        <div class="MoreHeader absolute no-select" v-if="state.more" @click="state.more = false"
             v-on-clickaway="() => {if(state.more) state.more = false}">
          <div class="border-3 bg-white w-100 elevation-2 index-top-elevation border">
            <div @click="onSave">Save</div>
            <div @click="onPreview">Preview</div>
            <div @click="state.delete = true">Delete</div>
          </div>
        </div>
      </div>

      <portal to="dialog-styled" v-if="state.delete">
        <h3>Are you sure?</h3>
        <p>Once the article is deleted, it cannot be recovered.</p>

        <div class="right">
          <button class="" @click="state.delete = false">Cancel</button>
          <button class="danger" @click="onDelete">Confirm</button>
        </div>
      </portal>

      <portal to="dialog-full" v-if="state.publish">
        <article-editor-publish @on-save="onSave" @on-cancel="state.publish = false" @on-publish="onPublish"
                                v-model="revision"/>
      </portal>
    </header-middle>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import ArticleEditor from "../../../components/article/ArticleEditor";
  import HeaderMiddle from "../../../components/layouts/HeaderMiddle";
  import ArticleEditorPublish from "../../../components/article/editor/ArticleEditorPublish";

  export default {
    components: {ArticleEditorPublish, HeaderMiddle, ArticleEditor},
    head() {
      const title = this.revision?.title || 'Article'
      return {title: `Editing ${title} · ${this.name}`}
    },
    computed: {
      ...mapGetters('account', ['name', 'username']),
    },
    data() {
      return {
        error: null,
        state: {
          more: false,
          delete: false,
          publish: false
        }
      }
    },
    asyncData({$api, params: {id}}) {
      if (id === 'new') {
        return {
          revision: {
            id: null,
            content: [
              {type: "heading", attrs: {level: 1}},
              {type: "paragraph",},
            ],
            title: null,
            description: null,
            tags: [],
            options: {
              map: true,
              ads: true,
              affiliate: true,
              placePublishing: true,
            },
            published: false
          }
        }
      }

      return $api.get(`/me/articles/${id}/revisions/latest`)
        .then(({data: revision}) => {
          return {revision}
        })
    },
    methods: {
      onSave(object) {
        if (this.error) return null

        this.revision.published = false
        let request
        if (this.revision.id) {
          request = this.$api.post(`/me/articles/${this.revision.id}/revisions`, this.revision)
            .then(({data: revision}) => {
              this.revision.uid = revision.uid
            })
        } else {
          request = this.$api.post(`/me/articles`, this.revision)
            .then(({data: article}) => {
              this.revision.id = article.id
              window.history.replaceState({}, document.title, `/me/articles/${article.id}`)
            });
        }

        return request.then(() => {
          this.error = null
          if (object && object.callback) object.callback()
        }).catch(error => {
          console.log(JSON.stringify(this.revision))

          this.error = error
          this.$store.dispatch('addError', {error, timeout: 86400000})
        })
      },
      onPreview() {
        this.$store.commit('global/setDialog', 'LoadingDialog')

        this.onSave()
          .then(() => {
            return setTimeout(() => {
              this.$store.commit('global/clearDialog');
              this.$router.push({
                path: `/@${this.username}/${this.revision.id}`,
                query: {uid: this.revision.uid}
              })
            }, 1000);
          })
          .catch((error) => {
            this.$store.commit('global/clearDialog')
            this.$store.dispatch('addError', error)
          });
      },
      onDelete() {
        this.$store.commit('global/setDialog', 'LoadingDialog')
        this.$api.patch(`/me/articles/${this.revision.id}`, {status: 'DELETED'})
          .then(() => {
            this.$store.commit('global/clearDialog')
            this.$router.push({path: `/me/articles`})
          })
          .catch((error) => {
            this.$store.commit('global/clearDialog')
            this.$store.dispatch('addError', error)
          });
      },
      onPublishStart() {
        // Fill in title, description to assist publishing
        if (!this.revision.title) {
          this.revision.title = this.revision.content
            .filter(value => value.type === 'heading' && value.content != null)
            .map(value => value.content.map(content => content.text).join(""))
            .filter(value => value)[0] || ""
        }

        if (!this.revision.description) {
          this.revision.description = this.revision.content
            .filter(value => value.type === 'paragraph' && value.content != null)
            .map(value => value.content.map(content => content.text).join(""))
            .filter(value => value)[0] || ""
        }

        this.state.publish = true
      },
      onPublish() {
        this.state.publish = false

        return this.$api.post(`/me/articles/${this.revision.id}/revisions/publish`, this.revision)
          .then(({data: revision}) => {
            this.$router.push({path: `/me/articles/published`})
          }).catch(error => {
            this.$store.dispatch('addError', error)
          })
      }
    }
  }
</script>

<style scoped lang="less">
  .MoreHeader {
    min-width: 160px;

    top: 40px;
    right: 0;

    > div {
      padding: 12px 0;

      > div {
        font-size: 15px;
        font-weight: 500;
        padding: 8px 24px;

        &:hover {
          cursor: pointer;
        }
      }
    }
  }
</style>
