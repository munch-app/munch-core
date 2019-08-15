<template>
  <portal to="dialog-full">
    <div class="container-768 mtb-64 bg-white p-24 elevation-1 border-3">
      <div class="flex-between">
        <h2 class="s500">Your published content</h2>
        <button class="border" @click="onClose">Close</button>
      </div>

      <div v-if="contents" class="mtb-16 ContentList">
        <div v-for="content in contents" :key="content.contentId" @click="onContent(content)"
             class="mtb-8 p-24-0 hr-bot hover-pointer">
          <h3>{{content.title || 'Untitled Content'}}</h3>
          <div class="b-a60">
            <div class="regular mb-8" v-if="content.body">{{content.body}}</div>
            <div class="subtext">Last edited on {{formatMillis(content.updatedMillis)}}</div>
          </div>
        </div>

        <div v-if="contents.length === 0" class="flex-center">
          <div class="p-32">
            <h3>You don't have any published content yet.</h3>
          </div>
        </div>

        <div class="flex-center mtb-32" v-if="next">
          <button class="blue-outline" @click="onLoadMore">Load More</button>
        </div>
      </div>
    </div>
  </portal>
</template>

<script>
  import dateformat from 'dateformat'
  import {mapGetters} from "vuex";

  export default {
    name: "SeriesSelectorContent",
    data() {
      return {contents: null}
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
    },
    mounted() {
      return this.$api.get(`/creators/${this.creatorId}/contents`, {
        params: {size: 50, index: 'published'}
      }).then(({data, next}) => {
        this.contents = data
        this.next = next
      })
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
      onLoadMore() {
        const params = {size: 50, index: 'published'}
        params['next.sortId'] = this.next.sortId

        this.$api.get(`/creators/${this.creatorId}/contents`, {params})
          .then(({data, next}) => {
            this.contents.push(...data)
            this.next = next
          })
      },
      onContent(content) {
        this.$emit('select', content)
      },
      onClose() {
        this.$emit('close')
      }
    }
  }
</script>

<style scoped lang="less">
  .ContentList {
    overflow-y: auto;

    height: calc(100vh - 128px - 128px);
  }
</style>
