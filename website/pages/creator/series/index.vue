<template>
  <div class="container-1200 mtb-32">
    <div class="flex-between">
      <div>
        <h1>Your series</h1>
        <h6><span class="s500">{{creatorName}}</span></h6>
      </div>
      <button class="blue-outline small" @click="onNewSeries">Create Series</button>
    </div>

    <div class="hr-bot mt-32 SeriesTabs flex-wrap no-select">
      <div class="hover-pointer" v-for="tab in tabs" :key="tab.type"
           :class="{Selected: selected === tab.type}"
           @click="onTab(tab.type)">
        {{tab.name}}
      </div>
    </div>

    <div v-if="series" class="mtb-16">
      <div v-for="s in series" :key="s.seriesId" @click="onSeries(s)"
           class="mtb-8 p-24-0 hr-bot hover-pointer">
        <h3>{{s.title || 'Untitled Series'}}</h3>
        <div class="b-a60">
          <div class="regular mb-8" v-if="s.body">{{s.body}}</div>
          <div class="subtext">Last edited on {{formatMillis(s.updatedMillis)}}</div>
        </div>
      </div>


      <div v-if="series.length === 0" class="flex-center">
        <div class="p-32">
          <h3 v-if="selected === 'draft'">You have no drafts.</h3>
          <h3 v-if="selected === 'published'">You don't have any published series yet.</h3>
          <h3 v-if="selected === 'archived'">You don't have any archived series yet.</h3>
        </div>
      </div>
    </div>

    <div class="flex-center mtb-32" v-if="next">
      <button class="blue-outline" @click="onLoadMore">Load More</button>
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
      ...mapGetters('creator', ['creatorName', 'creatorId']),
    },
    asyncData({$api, store}) {
      const creatorId = store.state.creator.profile.creatorId
      return $api.get(`/creators/${creatorId}/series`, {
        params: {size: 50, index: 'draft'}
      }).then(({data, next}) => {
        return {series: data, next}
      })
    },
    data() {
      return {
        tabs: [
          {name: 'Drafts', type: 'draft'},
          {name: 'Published', type: 'published'},
          {name: 'Archived', type: 'archived'},
        ],
        selected: 'draft'
      }
    },
    methods: {
      formatMillis: (millis) => dateformat(millis, 'mmm dd, yyyy'),
      onNewSeries() {
        this.$router.push({path: '/creator/series/new'})
      },
      onTab(type) {
        this.selected = type
        this.series = null

        this.$api.get(`/creators/${this.creatorId}/series`, {
          params: {size: 50, index: type}
        }).then(({data, next}) => {
          this.series = data
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

        this.$api.get(`/creators/${this.creatorId}/series`, {params})
          .then(({data, next}) => {
            this.series.push(...data)
            this.next = next
          })
      },
      onSeries(series) {
        this.$router.push({path: `/creator/series/${series.seriesId}`})
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
