<template>
  <div>
    <div v-if="item.placeId">
      <div v-if="loading">
        <p>Loading...</p>
      </div>
      <div v-else-if="place" class="PlaceCard">
        <place-card :place="place"/>
      </div>
      <div v-else>
        <h2>{{item.placeName}}</h2>
        <h3>Place Not Found</h3>
      </div>
    </div>

    <div v-else class="PlaceSearch bg-whisper050 p-24 border-3">
      <div class="bg-whisper050 mb-24">
        <input ref="input" class="TextBar bg-white wh-100 large" type="text" placeholder="Search Place Name"
               v-model="query">
      </div>

      <div class="PlaceList">
        <div v-for="place in places" :key="place.placeId" @click="onPlace(place)"
             class="PlaceItem flex hover-pointer flex-align-center">

          <image-sizes v-if="place.images.length > 0" :sizes="place.images[0].sizes"
                       width="100" object-fit="cover" class="wh-48px"/>
          <div v-else class="bg-whisper200 p-8 wh-48px small">No Image</div>

          <div class="ml-16">
            <h5 class="m-0">{{place.name}}</h5>
            <div class="small">{{place.location.address}}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from 'vue';

  import PlaceCard from "../../places/PlaceCard";
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "CreatorItemPlace",
    components: {ImageSizes, PlaceCard},
    props: {
      item: {
        type: Object,
        required: true,
        twoWay: true,
      }
    },
    data() {
      return {
        query: '',
        places: [],
        place: null,
        loading: true,
      }
    },
    methods: {
      onPlace(place) {
        this.place = place

        Vue.set(this.item, 'placeId', place.placeId)
        Vue.set(this.item, 'name', place.name)
        this.$emit('change')
      },
    },
    mounted() {
      this.$api.get(`/places/${this.item.placeId}`)
        .then(({data: {place}}) => {
          this.place = place
          this.loading = false
        })
        .catch((err) => {
          if (err.statusCode === 404) {
            this.loading = false
          } else {
            this.$store.dispatch('addError', err)
          }
        })
    },
    subscriptions() {
      const observable = this.$watchAsObservable('query').pipe(
        pluck('newValue'),
        tap(() => this.places = []),
        map((text) => text.trim()),
        filter((text) => text.length > 1),
        distinctUntilChanged(),
        debounceTime(333),
      )

      const places = observable.pipe(
        switchMap((text) => {
          return this.$axios.$post('/api/suggest', {"text": text, "searchQuery": {}}, {progress: false})
        }),
        map(({data: {places}}) => places)
      )

      return {places}
    }
  }
</script>

<style scoped lang="less">
  .PlaceList {
    overflow: auto;
    max-height: 320px;

    margin-top: -8px;
    margin-bottom: -8px;
  }

  .PlaceItem {
    padding-top: 8px;
    padding-bottom: 8px;
  }

  .PlaceCard {
    max-width: 240px;
  }
</style>
