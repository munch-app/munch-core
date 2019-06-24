<template>
  <div>
    <portal to="dialog-blank">
      <div class="SearchDialog bg-white" v-on-clickaway="onClose">
        <input ref="input" class="large p-16-24 hr-bot" type="text"
               placeholder="Search restaurant"
               v-model="query">

        <div class="PlaceList p-16-24">
          <div v-for="place in places" :key="place.placeId" @click="onPlace(place)"
               class="PlaceItem hover-pointer flex-align-center">

            <div class="flex-no-shrink overflow-hidden border-3">
              <image-sizes v-if="place.images.length > 0" :sizes="place.images[0].sizes"
                           width="100" object-fit="cover" class="wh-64px"/>
              <div v-else class="bg-whisper100 p-12 text-center b-a75 flex-center tiny wh-64px">No Image Available</div>
            </div>

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
  .SearchDialog {
    height: 50vh;
    min-width: 576px;
  }

  input {
    width: 100%;
    border: none;
    border-bottom: 1px solid rgba(0, 0, 0, .1);
  }

  .PlaceList {
    overflow: auto;
    height: calc(100% - 32px - 28px); // 32px - 28px
  }

  .PlaceItem {
    padding-top: 6px;
    padding-bottom: 6px;
  }
</style>
