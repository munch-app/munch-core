<template>
  <div>
    <masonry-lanes ref="masonry" :items="items" :options="options">
      <template v-slot:item="{item}">
        <pre>{{item.id}}</pre>
      </template>
    </masonry-lanes>
  </div>
</template>

<script>
  import MasonryLanes from "../utils/MasonryLanes";

  export default {
    name: "PlaceMentions",
    components: {MasonryLanes},
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        options: {
          width: 250,
          lanes: {
            2: {padding: 8}
          },
          min: 2,
          ssr: {default: 2}
        },

        api: {
          params: {
            types: 'MEDIA',
            size: 20,
            cursor: undefined
          }
        },

        items: [],
        loading: false,
      }
    },
    computed: {},
    mounted() {
      this.append()
    },
    methods: {
      append() {
        if (this.loading) return

        this.loading = true

        this.$api.get(`/places/${this.place.id}/mentions`, {params: this.api.params})
          .then(({data, cursor}) => {
            this.items.push(...data)
            this.api.params.cursor = cursor?.next

            this.loading = false
            this.$refs.masonry.fill()
          })
      }
    }
  }
</script>

<style scoped lang="less">

</style>
