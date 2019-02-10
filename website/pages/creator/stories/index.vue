<template>
  <div class="container-1200 mtb-32">
    <div class="flex-between">
      <div>
        <h1>Your content</h1>
        <h6><span class="s500">{{creatorName}}</span></h6>
      </div>
      <button class="secondary-outline small" @click="onNewStory">New Content</button>
    </div>

    <div class="hr-bot mt-32 ArticleTabs flex-wrap no-select">
      <div class="hover-pointer" v-for="tab in tabs" :key="tab.type"
           :class="{Selected: selected === tab.type}"
           @click="onTab(tab.type)">
        {{tab.name}}
      </div>
    </div>

    <div v-if="stories" class="mtb-16">
      <div v-for="story in stories" :key="story.storyId" @click="onStory(story)"
           class="mtb-8 p-24-0 hr-bot hover-pointer">
        <h3>{{story.title}}</h3>
        <div class="back-text">
          <div v-if="story.subtitle">{{story.subtitle}}</div>
          <div>Last edited on {{formatMillis(story.updatedMillis)}}</div>
        </div>
      </div>

      <div v-if="stories.length === 0" class="flex-center">
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
      return $api.get(`/creators/${creatorId}/stories`, {
        params: {size: 50, sort: 'statusDraft'}
      }).then(({data, next}) => {
        return {stories: data, next}
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
      onNewStory() {
        this.$router.push({path: '/creator/stories/new'})
      },
      onTab(type) {
        this.selected = type
        this.stories = null

        this.$api.get(`/creators/${this.creatorId}/stories`, {
          params: {size: 50, sort: type}
        }).then(({data, next}) => {
          this.stories = data
          this.next = next
        })
      },
      onLoadMore() {
        const params = {size: 50, sort: this.selected}

        switch (this.selected) {
          case 'statusDraft':
          case 'statusPublished':
            params['next.statusSort'] = this.next.statusSort
            break

          case 'typeAward':
          case 'typeGuide':
          case 'typeBlog':
            params['next.typeSort'] = this.next.typeSort
            break
        }

        this.$api.get(`/creators/${this.creatorId}/stories`, {params})
          .then(({data, next}) => {
            this.stories.push(...data)
            this.next = next
          })
      },
      onStory(story) {
        this.$router.push({path: `/creator/stories/${story.storyId}`})
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
