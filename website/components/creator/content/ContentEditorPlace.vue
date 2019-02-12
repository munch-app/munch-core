<template>
  <div class="ContentPlace" @click.capture.stop>
    <div v-if="loading">
      <p>Loading...</p>
    </div>
    <div v-else-if="place" class="PlaceCard">
      <place-card :place="place"/>
    </div>
    <div v-else class="p-16-24 bg-peach100 border-3">
      <h5>Previously Selected Place Not Found</h5>
      <h4>Name: {{placeName}}</h4>
      <div class="subtext m-0">Place is either deleted, removed or merged into another place.</div>
    </div>
  </div>
</template>

<script>
  import PlaceCard from "../../places/PlaceCard";

  export default {
    name: "ContentEditorPlace",
    components: {PlaceCard},
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
  .ContentPlace {
    margin-top: 32px;
    margin-bottom: 32px;
  }

  .PlaceCard {
    max-width: 240px;
    white-space: initial;
  }
</style>
