<template>
  <div class="Page container-768">
    <div v-if="payload.place.placeId">
      <div v-if="submitted">
        <h1 class="mt-8">Thank you for your contribution.</h1>
        <p class="mt-24">Own this restaurant? Do you want us to optimise your page?
          Drop us an email at <span class="weight-600">restaurant@munch.app</span> if there’s anything we can help
          with.
        </p>
      </div>
      <div class="zero" v-else>
        <div class="Header">
          <h2 v-if="payload.place.placeId">Suggest Edits: <span class="s700">{{payload.place.name}}</span></h2>
          <h2 v-else>Suggest a new edit</h2>
        </div>

        <div class="Navigation hr-bot flex">
          <div class="Tab border border-4-top weight-600 hover-pointer" v-for="tab in tabs" :key="tab.component"
               :class="{'bg-s400 white': selected === tab.component}" @click="selected = tab.component">
            {{tab.name}}
          </div>
        </div>

        <div class="Content">
          <keep-alive>
            <component :payload="payload" :is="selected"/>
          </keep-alive>
        </div>

        <div class="Action">
          <place-suggest-changes :payload="payload"/>

          <div class="flex-justify-end">
            <button class="primary" @click="onSubmit">Submit</button>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <h1 class="mt-8">Sorry, you can only suggest an edit for an existing place.</h1>
      <p class="mt-24">
        Drop us an email at <span class="weight-600">restaurant@munch.space</span> if there’s anything we can help
        with.
      </p>
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
  import PlaceSuggestMenu from "../../components/places/suggest/PlaceSuggestMenu";
  import PlaceSuggestChanges from "../../components/places/suggest/PlaceSuggestChanges";
  import DialogLoading from "../../components/layouts/DialogLoading";

  const newPayload = (data) => {
    const {place, images, articles} = data || {
      images: [], articles: [],
      place: {location: {}, price: {}, status: {type: 'open'}},
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
          type: place.status.type
        },
        // Implement in the future
        hours: [],
        tags: [],
        menu: {}
      },
      origin: place,
      removes: {
        images: {}, // imageId: {flag: ''}
        articles: {}, // articleId: {flag: ''}
      }
    }
  }

  export default {
    components: {
      DialogLoading,
      PlaceSuggestChanges,
      PlaceSuggestMenu, PlaceSuggestImage, PlaceSuggestArticle, PlaceSuggestDetail, ImageSizes
    },
    head() {
      const meta = []
      meta.push({name: 'robots', content: `follow,index`})
      meta.push({
        hid: 'description',
        name: 'description',
        content: 'Suggest an place edit on Munch. Drop us an email at restaurant@munch.space if there’s anything we can help with.'
      })
      return {title: 'Suggest Place · Munch - Social Dining App', meta,}
    },
    asyncData({$axios, params, route}) {
      const placeId = route.query.placeId
      if (placeId) {
        return $axios.$get(`/api/places/${placeId}`).then(({data}) => {
          return {payload: newPayload(data)}
        })
      } else {
        return {payload: newPayload()}
      }
    },
    data() {
      return {
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
          place: this.payload.place,
          removes: this.payload.removes,
        }
        return this.$axios.$post('/api/users/suggests/places', payload)
          .then(({data}) => {
            this.submitting = false
            this.submitted = true
          })
          .catch(error => {
            this.submitting = false
            this.$store.dispatch('addError', error)
          })
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
