<template>
  <div class="ContentPlace mt-48 mb-24" @click.capture.stop>
    <div v-if="loading">
      <p>Loading...</p>
    </div>
    <div v-else class="PlaceCard">
      <content-place :place="place" :place-id="placeId" :place-name="placeName"/>
    </div>
  </div>
</template>

<script>
  import PlaceCard from "../../places/PlaceCard";
  import ContentPlace from "../../../pages/contents/ContentPlace";

  export default {
    name: "ContentEditorPlace",
    components: {ContentPlace, PlaceCard},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      placeId: {
        get() {
          return this.node.attrs.placeId
        },
        set(placeId) {
          this.updateAttrs({
            placeId,
          })
        },
      },
      placeName: {
        get() {
          return this.node.attrs.placeName
        },
        set(placeName) {
          this.updateAttrs({
            placeName,
          })
        },
      },
    },
    data() {
      return {
        place: null,
        loading: true
      }
    },
    mounted() {
      this.$api.get(`/places/${this.placeId}`)
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
    }
  }
</script>

<style scoped lang="less">
  .PlaceCard {
    white-space: initial;
  }
</style>
