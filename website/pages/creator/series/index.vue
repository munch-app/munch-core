<template>
  <div class="container-1200 mtb-32">
    <div class="flex-between">
      <div>
        <h1>Your series</h1>
        <h6><span class="s500">{{creatorName}}</span></h6>
      </div>
      <button class="secondary-outline small" @click="onNewSeries">Create Series</button>
    </div>

    <div class="hr-bot mt-32 SeriesTabs flex-wrap no-select">
      <div class="hover-pointer" v-for="tab in tabs" :key="tab.type"
           :class="{Selected: selected === tab.type}"
           @click="onTab(tab.type)">
        {{tab.name}}
      </div>
    </div>

    <div v-if="series" class="mtb-32">
      <div v-for="s in series" :key="s.seriesId" class="mtb-24 hr-bot hover-pointer" @click="onSeries(s)">
        <h3>{{s.title}}</h3>
        <p v-if="s.body">{{s.body}}</p>
        <p class="subtext">Last edited </p>
      </div>

      <div v-if="series.length === 0" class="flex">
        <div class="p-24 bg-whisper100 border-4 flex-shrink">
          <h2>No Series Found</h2>
          <p class="s700 hover-pointer" @click="onNewSeries">Create a new series for {{creatorName}}.</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import dateformat from 'dateformat'
  import {mapGetters} from "vuex";

  export default {
    layout: 'creator',
    head() {
      return {title: `${this.creatorName} Series · Munch Creator`}
    },
    computed: {
      ...mapGetters('creator', ['creatorName']),
    },
    asyncData({$api, store}) {
      const creatorId = store.state.creator.profile.creatorId
      return $api.get(`/creators/${creatorId}/series`, {
        params: {
          size: 30,
          sort: 'statusDraft',
        }
      }).then(({data, next}) => {
        return {series: data, next}
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
      onNewSeries() {
        // TODO
      },
      onTab(type) {
      },
      onSeries(series) {

      }
    }
  }
</script>

<style scoped lang="less">
  .SeriesTabs {
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
