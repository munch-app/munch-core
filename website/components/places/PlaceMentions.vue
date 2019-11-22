<template>
  <div>
    <h3 v-if="hasAny">{{place.name}} Mentions</h3>

    <div class="mt-24">
      <masonry-lanes ref="masonry" :items="items" :options="options" @append="append">
        <template v-slot:item="{item}">
          <mention-card :mention="item"/>
        </template>
      </masonry-lanes>
    </div>

    <client-only>
      <div class="flex-center p-24" v-if="hasNext">
        <beat-loader color="#07F" size="16px"/>
      </div>
    </client-only>
  </div>
</template>

<script>
  import MasonryLanes from "../utils/MasonryLanes";
  import MentionCard from "../mention/MentionCard";

  export default {
    name: "PlaceMentions",
    components: {MentionCard, MasonryLanes},
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        options: {
          width: 300,
          lanes: {
            2: {padding: 8}
          },
          min: 2,
          ssr: {default: 2}
        },

        api: {
          params: {
            types: 'MEDIA,POST,ARTICLE',
            size: 20,
            cursor: undefined
          }
        },

        items: [],
        loading: false,
      }
    },
    computed: {
      hasAny() {
        return this.items.length > 0
      },
      hasNext() {
        if (this.api.params.cursor === undefined) {
          return true
        }
        return !!this.api.params.cursor
      }
    },
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
            this.api.params.cursor = cursor?.next || false

            this.loading = false
            this.$refs.masonry.fill()
          })
      }
    }
  }
</script>

<style scoped lang="less">

</style>
