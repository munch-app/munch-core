<template>
  <div class="container-768 pt-48 pb-128">
    <div class="bg-pink border-3 white mb-48">
      <div class="p-8-12 small">
        <b>NOTICE:</b> You are currently editing this article through your admin escalation.
      </div>
    </div>

    <article-editor v-model="revision" @on-save="onSave"/>

    <header-middle>
      <div class="flex-align-center flex-justify-end relative">
        <div class="mr-8">
          <button @click="onPublishStart" class="tiny pink-outline">Ready to publish?</button>
        </div>
        <div>
          <button @click="onMore" class="small">
            <simple-svg class="wh-20px" fill="black" :filepath="require('~/assets/icon/icons8-more.svg')"/>
          </button>
        </div>

        <div class="MoreHeader absolute no-select" v-if="state === 'more'" v-on-clickaway="onClose">
          <div class="border-3 bg-white w-100 elevation-2 index-top-elevation border">
            <div @click="onSave">Save</div>
            <div @click="onPreview">Preview</div>
            <div @click="onDelete">Delete</div>
          </div>
        </div>
      </div>

      <div class="fixed position-0 index-elevation" v-if="state === 'publish'">
        <article-editor-publish @on-save="onSave" @on-cancel="state = null" @on-publish="onPublish"
                                v-model="revision"/>
      </div>
    </header-middle>
  </div>
</template>

<script>
  import ArticleEditorPublish from "../../../../../components/article/editor/ArticleEditorPublish";
  import ArticleEditor from "../../../../../components/article/ArticleEditor";
  import HeaderMiddle from "../../../../../components/layouts/HeaderMiddle";

  export default {
    layout: 'system',
    components: {HeaderMiddle, ArticleEditor, ArticleEditorPublish},
    data() {
      return {
        error: null,
        state: null,
      }
    },
    asyncData({$api, params: {uid, id}}) {
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
          },
          profile: {uid}
        }
      }

      return $api.get(`/admin/profiles/${uid}/articles/${id}/revisions/latest`)
        .then(({data: revision}) => {
          console.log(revision)
          return {revision, profile: revision.profile}
        })
    },
    methods: {
      onMore() {
        if (this.state === 'more') {
          this.state = null
        } else {
          this.state = 'more'
        }
      },
      onClose() {
        if (this.state === 'more') {
          this.state = null
        }
      },
      onSave(object) {
        this.state = null
        if (this.error) return null

        this.revision.published = false
        let request
        if (this.revision.id) {
          request = this.$api.post(`/admin/profiles/${this.profile.uid}/articles/${this.revision.id}/revisions`, this.revision)
            .then(({data: revision}) => {
              this.revision.uid = revision.uid
            })
        } else {
          request = this.$api.post(`/admin/profiles/${this.profile.uid}/articles`, this.revision)
            .then(({data: article}) => {
              this.revision.id = article.id
              window.history.replaceState({}, document.title, `/system/profiles/${this.profile.uid}/articles/${article.id}`)
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
                path: `/@${this.profile.username}/${this.revision.id}`,
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
        this.$store.commit('global/setDialog', {
          name: 'ConfirmationDialog', props: {
            title: 'Are you sure?',
            message: 'Once the article is deleted, it cannot be recovered.',
            options: {
              confirm: {class: 'danger'},
            },
            onConfirm: () => {
              this.$store.commit('global/setDialog', 'LoadingDialog')
              this.$api.patch(`/admin/profiles/${this.profile.uid}/articles/${this.revision.id}`, {status: 'DELETED'})
                .then(() => {
                  this.$router.push({path: `/system/profiles/${this.profile.uid}/articles/draft`})
                })
                .catch((error) => {
                  this.$store.dispatch('addError', error)
                })
                .finally(() => {
                  this.$store.commit('global/clearDialog')
                })
            },
          }
        })
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

        this.state = 'publish'
      },
      onPublish() {
        this.state = null

        return this.$api.post(`/admin/profiles/${this.profile.uid}/articles/${this.revision.id}/revisions/publish`, this.revision)
          .then(({data: revision}) => {
            this.$router.push({path: `/system/profiles/${this.profile.uid}/articles/published`})
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
