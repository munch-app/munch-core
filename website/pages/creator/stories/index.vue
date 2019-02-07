<template>
  <div class="container-1200 mtb-32">
    <div class="flex-between">
      <h1>{{creatorName}} Stories</h1>
      <button class="secondary-outline small" @click="onNewStory">Write a story</button>
    </div>

    <div class="hr-bot mt-32 ArticleTabs flex-wrap no-select">
      <div class="hover-pointer" v-for="tab in tabs" :key="tab.type"
           :class="{Selected: selected === tab.type}"
           @click="onTab(tab.type)">
        {{tab.name}}
      </div>
    </div>

    <div v-if="stories">
      <div v-for="story in stories" :key="story.storyId"
           class="mtb-24 hr-bot hover-pointer"
           @click="onStory(story)">
        <h3>{{story.title}}</h3>
        <p v-if="story.subtitle">{{story.subtitle}}</p>
        <p class="subtext">Last edited </p>
      </div>

      <div class="mtb-24 p-16-24 bg-whisper100 border-4" v-if="stories.length === 0">
        <h2>No stories found</h2>
        <p class="text-underline hover-pointer" @click="onNewStory">Write a new story for {{creatorName}}.</p>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";

  export default {
    layout: 'creator',
    head() {
      return {title: `${this.creatorName} Articles · Munch Creator`}
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
    },
    asyncData({$api, store}) {
      const creatorId = store.state.creator.profile.creatorId
      return $api.get(`/creators/${creatorId}/stories`, {
        params: {
          size: 30,
          sort: 'statusDraft',
        }
      }).then(({data, next}) => {
        return {stories: data, next}
      })
    },
    data() {
      return {
        tabs: [
          {name: 'Drafts', type: 'statusDraft'},
          {name: 'Published', type: 'statusPublished'},

          {name: 'Guides', type: 'typeGuide'},
          {name: 'Awards', type: 'typeAward'},
          {name: 'Blog', type: 'typeBlog'},
        ],
        selected: 'statusDraft'
      }
    },
    methods: {
      onNewStory() {
        this.$router.push({path: '/creator/stories/new'})
      },
      onTab(type) {
        this.selected = type
        this.stories = null

        this.$api.get(`/creators/${this.creatorId}/stories`, {
          params: {
            size: 30,
            sort: type,
          }
        }).then(({data, next}) => {
          this.stories = data
          this.next = next
        })
      },
      onStory(story) {
        this.$router.push({path: `/creator/stories/${story.storyId}`})
      }
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
