<template>
  <div class="Page container-768">
    <div>
      <div v-if="submitted">
        <h1 class="mt-8">Thank you for your contribution.</h1>
        <p class="mt-24">Own this restaurant? Do you want us to optimise your page?
          Drop us an email at <span class="weight-600">restaurant@munch.app</span> if there’s anything we can help
          with.
        </p>
      </div>
      <div class="zero" v-else>
        <div class="Header">
          <div v-if="payload.place.placeId">
            <h5>Suggest Edits:</h5>
            <h1>{{payload.place.name}}</h1>
          </div>
          <h2 v-else>Suggest a new place</h2>
        </div>

        <div class="Content">
          <place-suggest-detail :payload="payload"/>
          <place-suggest-image :payload="payload"/>
          <place-suggest-article :payload="payload" v-if="payload.articles.length > 0"/>
        </div>

        <div class="Action">
          <place-suggest-changes :payload="payload"></place-suggest-changes>
          <div class="flex-justify-end">
            <button class="primary mt-24" @click="onSubmit"
                    v-if="payload.place.name.trim().length > 0 && payload.place.location.address.trim().length">Submit
            </button>
            <button class="primary mt-24 disabled" v-else>Submit</button>
          </div>
        </div>
      </div>
    </div>
    <no-ssr>
      <dialog-loading v-if="submitting"/>
    </no-ssr>
  </div>
</template>

<script>
  import ImageSizes from "../../components/core/ImageSizes";
  import PlaceSuggestDetail from "../../components/places/suggest/PlaceSuggestDetail";
  import PlaceSuggestArticle from "../../components/places/suggest/PlaceSuggestArticle";
  import PlaceSuggestImage from "../../components/places/suggest/PlaceSuggestImage";
  import PlaceSuggestChanges from "../../components/places/suggest/PlaceSuggestChanges";
  import DialogLoading from "../../components/layouts/DialogLoading";
  import _ from "lodash";

  const newPayload = (data) => {
    const {place, images, articles} = data || {
      images: [], articles: [],
      place: {location: {}, price: {}, status: {type: 'open'}, menu: {}},
    }

    return {
      // images and articles are helper payload
      images,
      articles,
      // Editable Place Data
      place: {
        placeId: place.placeId, // Validity of PlaceId will determine if new edit or old edit
        name: place.name || '',
        alias: [],
        description: place.description || '',
        website: place.website || '',
        phone: place.phone || '',
        location: {
          address: place.location.address || '',
          latLng: place.location.latLng
        },
        price: {
          perPax: place.price && place.price.perPax || 0
        },
        status: {
          type: place.status.type || '',
          placeIds: null,
          placeNames: null,
        },
        tags: place.tags || [],
        hours: place.hours || [],
        menu: {
          url: place.menu && place.menu.url || '',
        }
      },
      uploads: {
        images: {}
      },
      removes: {
        images: {}, // imageId: {flag: ''}
        articles: {}, // articleId: {flag: ''}
      }
    }
  }

  export default {
    components: {
      DialogLoading, PlaceSuggestChanges, PlaceSuggestImage, PlaceSuggestArticle, PlaceSuggestDetail, ImageSizes
    },
    head() {
      return this.$head({
        robots: {follow: false, index: true},
        graph: {
          title: `Suggest an edit · Munch - Social Dining App`,
        },
      })
    },
    asyncData({$axios, params, route}) {
      const placeId = route.query.placeId
      if (placeId) {
        return $axios.$get(`/api/places/${placeId}`).then(({data}) => {

          data.place.tags = data.place.tags.map(tag => {
            return {text: tag.name}
          })

          return {originalPlace: {...data.place}, payload: newPayload(data)}
        })
      } else {
        return {originalPlace: newPayload().place, payload: newPayload()}
      }
    },
    data() {
      return {
        submitting: false,
        submitted: false
      }
    },
    methods: {
      onSubmit() {
        if (window) window.scrollTo(0, 0)
        this.submitting = true
        const form = new FormData()
        form.append('json', JSON.stringify({"changes": this.verifyFields()}))

        for (const key in this.payload.uploads.images) {
          if (this.payload.uploads.images.hasOwnProperty(key)) {
            const image = this.payload.uploads.images[key]
            form.append('images', image, image.name)
          }
        }

        return this.$api.post(`/places/${this.$route.query.placeId || "_"}/suggest/multipart`, form)
          .then((data) => {
            this.submitting = false
            this.submitted = true
          })
          .catch(error => {
            this.submitting = false
            this.$store.dispatch('addError', error)
          })
      },
      verifyFields() {
        const updatedData = []

        if (this.payload.place.name.trim() !== this.originalPlace.name) {
          updatedData.push(this.getChangeJSON(this.payload.place.name.trim(), "Replace", "Name"))
        }

        if ((this.payload.place.location && this.payload.place.location.address) && this.payload.place.location.address.trim() !== (this.originalPlace.location && this.originalPlace.location.address || '')) {
          updatedData.push(this.getChangeJSON(this.payload.place.location.address.trim(), "Replace", "LocationAddress"))
        }

        if ((this.payload.place.price && this.payload.place.price.perPax) && this.payload.place.price.perPax !== (this.originalPlace.price && this.originalPlace.price.perPax || 0)) {
          let convertedPrice = _.toNumber(this.payload.place.price.perPax)
          if (convertedPrice && !_.isNaN(convertedPrice) && convertedPrice > 0) {
            updatedData.push(this.getChangeJSON(convertedPrice, "Replace", "PricePerPax"))
          }
        }

        // Phone Change
        const phone = this.payload.place.phone.trim()
        const originPhone = this.originalPlace.phone
        if (phone === '') { // Phone: '', Origin: null
          if (originPhone != null) {
            updatedData.push(this.getChangeJSON(phone, "Remove", "Phone"))
          }
        } else if (phone !== originPhone) {
          updatedData.push(this.getChangeJSON(phone, "Replace", "Phone"))
        }

        // Website Change
        const website = this.payload.place.website.trim()
        const originWebsite = this.originalPlace.website
        if (website === '') {
          if (originWebsite != null) {
            updatedData.push(this.getChangeJSON(website, "Remove", "Website"))
          }
        } else if (website !== originWebsite) {
          updatedData.push(this.getChangeJSON(website, "Replace", "Website"))
        }


        const tagsAppend = _.differenceBy(this.payload.place.tags, this.originalPlace.tags, 'text')
        const tagsRemoval = _.differenceBy(this.originalPlace.tags, this.payload.place.tags, 'text')

        tagsAppend.forEach((tag) => {
          updatedData.push(this.getChangeJSON(tag.text.trim(), "Append", "Tag"))
        });

        tagsRemoval.forEach((tag) => {
          updatedData.push(this.getChangeJSON(tag.text.trim(), "Remove", "Tag"))
        });

        if (this.payload.place.menu.url.trim() !== (this.originalPlace.menu && this.originalPlace.menu.url || '')) {
          updatedData.push(this.getChangeJSON(this.payload.place.menu.url.trim(), "Replace", "MenuUrl"))
        }

        if (this.payload.place.description.trim() !== (this.originalPlace.description || '')) {
          updatedData.push(this.getChangeJSON(this.payload.place.description.trim(), "Replace", "Description"))
        }

        if (this.payload.place.status.type.trim() !== (this.originalPlace.status.type.trim() || '')) {
          if (this.payload.place.status.type !== 'duplicated') {
            this.payload.place.status.placeIds = null
            this.payload.place.status.placeNames = null
          }

          updatedData.push({
            statusType: this.payload.place.status.type,
            placeIds: (this.payload.place.status.placeIds || null),
            operation: "Replace",
            type: "Status"
          })
        }

        for (const key in this.payload.removes.articles) {
          if (this.payload.removes.articles.hasOwnProperty(key)) {
            const article = this.payload.removes.articles[key]
            updatedData.push({
              articleId: article.article.articleId,
              url: article.article.url,
              flagAs: article.flag,
              operation: "Remove", type: "Article"
            })
          }
        }

        for (const key in this.payload.removes.images) {
          if (this.payload.removes.images.hasOwnProperty(key)) {
            const image = this.payload.removes.images[key]
            updatedData.push({
              image: {
                imageId: image.image.imageId,
                url: image.image.url,
                sizes: image.image.sizes
              },
              flagAs: image.flag,
              operation: "Remove", type: "Image"
            })
          }
        }

        if (!_(this.originalPlace.hours).xorWith(this.payload.place.hours, _.isEqual).isEmpty()) {
          updatedData.push({
            hours: this.payload.place.hours,
            operation: "Replace",
            type: "HourList"
          })
        }

        //Alias
        if (this.payload.place.alias.length > 0) {
          updatedData.push(this.getChangeJSON(this.payload.place.name.trim(), "Replace", "Name"))
        }
        for (const alias of this.payload.place.alias) {
          updatedData.push({
            value: alias,
            operation: "Append",
            type: "Name"
          })
        }

        return updatedData
      },
      getChangeJSON(value, operation, type) {
        return {
          value: value,
          operation: operation,
          type: type
        }
      },
    }
  }
</script>

<style scoped lang="less">
  .Page {
    margin-top: 32px;
    margin-bottom: 64px;
  }

  .Header {
    h4 {
      margin-top: 8px;
    }
  }

  .Content {
    margin-top: 24px;
  }

  .Action {
    margin-top: 48px;
  }
</style>
