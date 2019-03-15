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
          <h2>Create a new place</h2>
        </div>

        <div class="Content">
          <place-suggest-detail :payload="payload"></place-suggest-detail>
          <!--<place-suggest-opening-hours :payload="payload"></place-suggest-opening-hours>-->
          <place-suggest-image :payload="payload" v-if="payload.images.length > 0"></place-suggest-image>
          <place-suggest-article :payload="payload" v-if="payload.articles.length > 0"></place-suggest-article>
        </div>

        <div class="Action">
          <place-suggest-changes :payload="payload"/>
          <div class="flex-justify-end">
            <button v-if="isSubmittable" @click="onSubmit" class="primary">Submit</button>
            <button v-else class="primary disabled">Submit</button>
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
  import PlaceSuggestOpeningHours from "../../components/places/suggest/PlaceSuggestOpeningHours";
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
          type: place.status.type,
          placeIds: null,
          placeNames: null,
        },
        tags: place.tags || [],
        hours: place.hours,
        menu: {
          url: place.menu && place.menu.url || '',
        }
      },
      removes: {
        images: {}, // imageId: {flag: ''}
        articles: {}, // articleId: {flag: ''}
      }
    }
  }

  export default {
    components: {
      PlaceSuggestOpeningHours,
      DialogLoading,
      PlaceSuggestChanges, PlaceSuggestImage, PlaceSuggestArticle, PlaceSuggestDetail, ImageSizes
    },
    head() {
      const meta = []
      meta.push({name: 'robots', content: `follow,index`})
      meta.push({
        hid: 'description',
        name: 'description',
        content: 'Suggest an place edit on Munch. Drop us an email at restaurant@munch.space if there’s anything we can help with.'
      })
      return {title: 'Suggest Place · Munch Singapore', meta,}
    },
    computed: {
      isSubmittable() {
        return (this.payload.place.name.trim().length > 2 && this.payload.place.location.address.trim().length > 0)
      }
    },
    data() {
      return {
        payload: newPayload(),
        selected: 'PlaceSuggestDetail',
        tabs: [
          {name: 'Detail', component: 'PlaceSuggestDetail'},
          // {name: 'Menu', component: 'PlaceSuggestMenu'},
          {name: 'Image', component: 'PlaceSuggestImage'},
          {name: 'Article', component: 'PlaceSuggestArticle'},
        ],
        submitting: false,
        submitted: false
      }
    },
    methods: {
      onSubmit() {
        if (window) window.scrollTo(0, 0)
        this.submitting = true

        const payload = {
          place: this.verifyFields(),
          // removes: this.payload.removes,
        }
        // return this.$axios.$post('/api/users/suggests/places', payload)
        //   .then(({data}) => {
        //     this.submitting = false
        //     this.submitted = true
        //   })
        //   .catch(error => {
        //     this.submitting = false
        //     this.$store.dispatch('addError', error)
        //   })
      },
      verifyFields() {
        const updatedData = []

        if (this.payload.place.name.trim().length > 2) {
          updatedData.push(this.getChangeJSON(this.payload.place.name.trim(), "Replace", "Name"))
        }

        if ((this.payload.place.location && this.payload.place.location.address) && this.payload.place.location.address.trim().length > 0) {
          updatedData.push(this.getChangeJSON(this.payload.place.location.address.trim(), "Replace", "LocationAddress"))
        }

        if ((this.payload.place.price && this.payload.place.price.perPax) && this.payload.place.price.perPax.trim()) {
          let convertedPrice = _.toNumber(this.payload.place.price.perPax.trim())
          if (convertedPrice && !_.isNaN(convertedPrice) && convertedPrice > 0) {
            updatedData.push(this.getChangeJSON(convertedPrice, "Replace", "PricePerPax"))
          }
        }

        if (this.payload.place.phone.trim()) {
          updatedData.push(this.getChangeJSON(this.payload.place.phone.trim(), "Replace", "Phone"))
        }

        this.payload.place.tags.forEach((tag) => {
          updatedData.push(this.getChangeJSON(tag.text.trim(), "Append", "Tag"))
        });

        if (this.payload.place.menu.url.trim()) {
          updatedData.push(this.getChangeJSON(this.payload.place.menu.url.trim(), "Replace", "MenuUrl"))
        }

        if (this.payload.place.description.trim()) {
          updatedData.push(this.getChangeJSON(this.payload.place.description.trim(), "Replace", "Description"))
        }

        if (this.payload.place.status.type.trim()) {
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

        console.log(JSON.stringify(updatedData))

        // for (const key in this.payload.removes.articles) {
        //   if (this.payload.removes.articles.hasOwnProperty(key)) {
        //     const article = this.payload.removes.articles[key]
        //     updatedData.push(this.getChangeJSON({articleId: article.articleId, url: article.url}, "Remove", "Article"))
        //   }
        // }
        //
        // this.payload.removes.articles.forEach((article) => {
        //   console.log(article)
        //   updatedData.push(this.getChangeJSON({articleId: article.articleId}, "Remove", "Tag"))
        // });

        return updatedData
      },
      getChangeJSON(value, operation, type) {
        return {
          value: value,
          operation: operation,
          type: type
        }
      }
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

  .Navigation {
    margin-top: 24px;
    overflow-x: scroll;

    .Tab {
      font-size: 15px;
      line-height: 20px;
      padding: 8px 16px;
      margin-right: 16px;

      border-style: solid solid none solid;
    }
  }

  .Content {
    margin-top: 24px;
  }

  .Action {
    margin-top: 48px;
  }
</style>
