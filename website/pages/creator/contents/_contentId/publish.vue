<template>
  <div class="container-1200 mtb-48">
    <h1 class="s500">Publishing Content Preview</h1>

    <div class="PublishEditor flex">
      <div>
        <div class="aspect r-5-2 border-3 overflow-hidden">
          <image-sizes v-if="content.image" :sizes="content.image.sizes"/>
          <div v-else class="bg-s500 flex-end wh-100"/>

          <div class="absolute-0 flex-center hover-pointer hover-bg-a40" @click="show.image = true">
            <div>
              <h3 class="white">Select Image</h3>
            </div>
          </div>
        </div>

        <text-auto class="h2 mt-16" v-model="content.title" placeholder="Title"/>
        <text-auto class="regular" v-model="content.body" placeholder="Body"/>
      </div>

      <div>
        <text-auto class="h5" v-model="content.subtitle" placeholder="Subtitle"/>
        <text-auto class="h4" v-model="parsed.tags" placeholder="Tags (comma separated)"/>

        <div class="flex mt-24">
          <button class="secondary mr-24" @click="onPublish()">Publish Now</button>
          <button class="" @click="onCancel">Cancel</button>
        </div>

        <div class="mt-48">
          <h3>Publish as Linked Place</h3>
          <p class="small">Linked place will show up on RIP.</p>
          <button class="secondary-outline" @click="onPublish('award')">Publish as Award</button>
        </div>
      </div>
    </div>

    <div class="mtb-64">
      <h2 class="b-a60">Content in Series Preview</h2>
      <div class="mt-24 ContentInSeries flex">
        <div v-for="option in [{}, {}, {}]" class="flex-grow">
          <search-series-content-card :content="content" :options="option"/>
        </div>
      </div>
    </div>

    <div class="mtb-64">
      <h2 class="b-a60">Mobile Preview</h2>
      <div class="mt-24 MobileContentPreview">
        <image-sizes v-if="content.image" :sizes="content.image.sizes"/>
        <h5 class="mt-8 s700">{{content.subtitle}}</h5>
        <h2 class="mt-8">{{content.title}}</h2>
        <p class="mt-24">{{content.body}}</p>

        <div class="regular text-center mt-32 p-16">More Content …</div>
        <div class="regular text-center p-16">More Content …</div>
      </div>
    </div>

    <content-nav-header @delete="deleteContent" publish/>

    <div class="absolute" v-show="false">
      <input ref="fileInput" type="file" accept="image/x-png,image/gif,image/jpeg" @change="onFileChanged">
    </div>

    <portal to="dialog-w768" v-if="show.image">
      <div v-on-clickaway="onAwayImage" class="zero">
        <div>
          <button @click="onUploadImage" class="secondary-outline m-0">
            Upload Image
          </button>
        </div>

        <div v-if="images.length > 0">
          <h4 class="mtb-24">Select Image from Content</h4>
          <div v-for="image in images" @click="onSelectImage(image)">
            <div class="aspect r-10-3 border-3 overflow-hidden mtb-24">
              <image-sizes :sizes="image.sizes"/>
            </div>
          </div>
        </div>

        <h4 class="mt-24 mb-16">Select Image from Place</h4>
        <div v-if="places">
          <div v-for="place in places">
            <h4 class="mt-24 mb-16">{{place.place.name}}</h4>
            <div class="flex-wrap SelectImageList">
              <div class="SelectImage" v-for="image in place.images" @click="onSelectImage(image)">
                <div class="aspect r-5-2 border-3 overflow-hidden">
                  <image-sizes width="1" :sizes="image.sizes"/>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <button class="secondary-outline small" @click="loadPlaceImages">
            Load Place Images
          </button>
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import _ from 'lodash'
  import Vue from 'vue'
  import {mapGetters} from "vuex"
  import ContentNavHeader from "../../../../components/creator/content/ContentNavHeader";
  import TextAuto from "../../../../components/core/TextAuto";
  import SearchSeriesContentCard from "../../../../components/search/cards/content/SearchSeriesContentCard";
  import ImageSizes from "../../../../components/core/ImageSizes";

  export default {
    components: {ImageSizes, SearchSeriesContentCard, TextAuto, ContentNavHeader},
    layout: 'creator',
    head() {
      const title = this.content && this.content.title || 'Content'
      return {title: `Publishing ${title} · ${this.creatorName}`}
    },
    data() {
      return {
        show: {
          image: false,
        },
        places: null
      }
    },
    asyncData({$api, params: {contentId}, $error}) {
      return $api.get(`/creators/_/contents/${contentId}/drafts`)
        .then(({data: {content, draft}}) => {
          return {
            content, draft, parsed: {
              tags: content.tags.join(','),
              linked: {type: null}
            }
          }
        }).catch((err) => $error(err))
    },
    computed: {
      ...mapGetters('creator', ['creatorName']),
      creatorId() {
        return this.content && this.content.creatorId || this.$store.getters['creator/creatorId']
      },
      contentId() {
        return this.content && this.content.contentId
      },
      images() {
        const content = this.draft.content
        const images = _.filter(content, c => c.type === 'image')
        return images.map(value => {
          return value.attrs.image
        })
      },
      placeItems() {
        const content = this.draft.content
        const images = _.filter(content, c => c.type === 'place')
        return images.map(value => value.attrs)
      },
    },
    methods: {
      onPublish(linkedType) {
        const tags = this.parsed.tags.split(',').map(t => t.trim())

        const body = {
          title: this.content.title,
          subtitle: this.content.subtitle,
          body: this.content.body,
          image: this.content.image,
          tags: _.filter(tags, t => t.length > 0)
        }

        if (linkedType) {
          body['linked'] = {type: linkedType}
        }

        return this.$api.patch(`/creators/${this.creatorId}/contents/${this.contentId}/drafts/publish`, body)
          .then(() => {
            this.$router.push({path: '/creator/contents'})
            this.$store.dispatch('addMessage', {message: `Published ${this.content.title}`})
          })
          .catch((err) => this.$store.dispatch('addError', err))
      },
      onCancel() {
        if (this.hasPrevious) {
          this.$router.go(-1)
        } else {
          this.$router.push({path: `/creator/contents/${this.contentId}`})
        }
      },
      deleteContent() {
        return this.$api.delete(`/creators/${this.creatorId}/contents/${this.contentId}`).then(() => {
          this.$router.push({path: '/creator/contents'})
        }).catch((err) => this.$store.dispatch('addError', err))
      },
      loadPlaceImages() {
        this.places = []

        this.placeItems.forEach(({placeId}) => {
          return this.$api.get(`/places/${placeId}`)
            .then(({data}) => {
              this.places.push(data)
            })
        })
      },
      onAwayImage() {
        this.show.image = false
      },
      onSelectImage(image) {
        this.content.image = image
        this.show.image = false
      },
      onUploadImage() {
        this.$refs.fileInput.click()
      },
      onFileChanged(event) {
        const file = event.target.files[0]

        const form = new FormData()
        form.append('file', file, file.name)

        return this.$api.post(`/creators/${this.creatorId}/contents/${this.contentId}/images`, form)
          .then(({data}) => {
            this.content.image = data
            this.show.image = false
          })
          .catch((err) => {
            this.$store.dispatch('addError', err)
          })
      },
    }
  }
</script>

<style scoped lang="less">
  .PublishEditor {
    margin-top: -24px;
    margin-left: -48px;
    margin-right: -48px;

    > div {
      padding: 48px;
      flex-basis: 50%;
    }
  }

  .ContentInSeries {
    margin-left: -12px;
    margin-right: -12px;

    > div {
      padding-left: 12px;
      padding-right: 12px;
    }
  }

  .MobileContentPreview {
    max-width: 320px;
  }

  .SelectImageList {
    margin: -8px;
  }

  .SelectImage {
    flex-basis: 25%;
    max-width: 25%;
    padding: 8px;
  }
</style>
