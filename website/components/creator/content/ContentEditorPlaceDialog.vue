<template>
  <div>
    <portal to="dialog-w400">
      <div class="PlaceSearch" v-on-clickaway="onClose">
        <div class="bg-whisper050 mb-24">
          <input ref="input" class="TextBar bg-white wh-100 large" type="text" placeholder="Search Place Name"
                 v-model="query">
        </div>

        <div class="PlaceList">
          <div v-for="place in places" :key="place.placeId" @click="onPlace(place)"
               class="PlaceItem flex hover-pointer flex-align-center">

            <image-sizes v-if="place.images.length > 0" :sizes="place.images[0].sizes"
                         width="100" object-fit="cover" class="wh-48px flex-no-shrink"/>
            <div v-else class="bg-whisper200 p-8 wh-48px small">No Image</div>

            <div class="ml-16">
              <h5 class="m-0">{{place.name}}</h5>
              <div class="small">{{place.location.address}}</div>
            </div>
          </div>
        </div>
      </div>
    </portal>
  </div>
</template>

<script>
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import ImageSizes from "../../core/ImageSizes";

  export default {
    name: "ContentEditorPlaceDialog",
    components: {ImageSizes},
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
        this.$emit('selected', place)
      },
      onClose() {
        this.$emit('close')
      }
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
</style>
