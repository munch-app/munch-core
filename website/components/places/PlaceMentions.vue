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
      <div class="flex-center p-24" v-if="hasNext && loading">
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
      },
      mentions: {
        type: Array,
        required: true
      },
      cursor: {
        type: String
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
            size: 20,
            cursor: this.cursor
          }
        },

        items: [...this.mentions],
        loading: false,
      }
    },
    computed: {
      hasAny() {
        return this.items.length > 0
      },
      hasNext() {
        return !!this.api.params.cursor
      }
    },
    methods: {
      append() {
        if (!this.hasNext) {
          return
        }

        if (this.loading) return
        this.loading = true


        this.$api.get(`/places/${this.place.id}/mentions`, {params: this.api.params})
          .then(({data, cursor}) => {
            this.items.push(...data)
            this.api.params.cursor = cursor?.next || false

            this.loading = false
          })
      }
    }
  }
</script>

<style scoped lang="less">

</style>
