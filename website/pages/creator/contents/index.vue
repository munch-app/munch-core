<template>
  <div class="container-1200 mtb-32">
    <div class="flex-between">
      <div>
        <h1>Your content</h1>
        <h6><span class="s500">{{creatorName}}</span></h6>
      </div>
      <button class="secondary-outline small" @click="onNewContent">New Content</button>
    </div>

    <div class="hr-bot mt-32 ArticleTabs flex-wrap no-select">
      <div class="hover-pointer" v-for="tab in tabs" :key="tab.type"
           :class="{Selected: selected === tab.type}"
           @click="onTab(tab.type)">
        {{tab.name}}
      </div>
    </div>

    <div v-if="contents" class="mtb-16">
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
          <h3 v-if="selected === 'statusDraft'">You have no drafts.</h3>
          <h3 v-if="selected === 'statusPublished'">You don't have any published content yet.</h3>
        </div>
      </div>
    </div>

    <div class="flex-center mtb-32" v-if="next">
      <button class="secondary-outline" @click="onLoadMore">Load More</button>
    </div>
  </div>
</template>

<script>
  import dateformat from 'dateformat'
  import {mapGetters} from "vuex";

  export default {
    layout: 'creator',
    head() {
      return {title: `${this.creatorName} Content · Munch Creator`}
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
    },
    asyncData({$api, store}) {
      const creatorId = store.state.creator.profile.creatorId
      return $api.get(`/creators/${creatorId}/contents`, {
        params: {size: 50, index: 'draft'}
      }).then(({data, next}) => {
        return {contents: data, next}
      })
    },
    data() {
      return {
        tabs: [
          {name: 'Drafts', type: 'statusDraft'},
          {name: 'Published', type: 'statusPublished'},
        ],
        selected: 'statusDraft'
      }
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
      onNewContent() {
        this.$router.push({path: '/creator/contents/new'})
      },
      onTab(type) {
        this.selected = type
        this.contents = null

        this.$api.get(`/creators/${this.creatorId}/contents`, {
          params: {size: 50, index: type}
        }).then(({data, next}) => {
          this.contents = data
          this.next = next
        })
      },
      onLoadMore() {
        const params = {size: 50, index: this.selected}

        switch (this.selected) {
          case 'draft':
          case 'published':
          case 'archived':
            params['next.sortId'] = this.next.sortId
            break
        }

        this.$api.get(`/creators/${this.creatorId}/contents`, {params})
          .then(({data, next}) => {
            this.contents.push(...data)
            this.next = next
          })
      },
      onContent(content) {
        this.$router.push({path: `/creator/contents/${content.contentId}`})
      },
    },
  }
</script>

<style scoped lang="less">
  .ArticleTabs {
    div {
      line-height: 1;
      margin-right: 24px;

      color: rgba(0, 0, 0, 0.7);

      &.Selected {
        color: black;
        padding-bottom: 12px;
        border-bottom: 1px solid black;
      }
    }
  }
</style>
