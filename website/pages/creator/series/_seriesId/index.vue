<template>
  <div class="container-1200 mtb-48 relative">
    <div>
      <text-auto class="h2" v-model="series.title" placeholder="Title"/>
      <text-auto class="h5" v-model="series.subtitle" placeholder="Subtitle"/>
      <text-auto class="regular" v-model="series.body" placeholder="Body"/>
    </div>

    <div>
      <div class="flex-between flex-align-center mb-24">
        <h1 class="s500 mt-24">Content in Series</h1>
        <button v-if="seriesId" class="border" @click="selecting = true">Add</button>
      </div>

      <div class="ContentInSeries flex-wrap" v-if="contents">
        <content-in-series v-for="content in contents" :key="content.contentId" :content-id="content.contentId"
                           :sort-id="content.sortId"
                           @change-sort="(s) => patchContent(content.contentId, s)"
                           @delete="deleteContent(content.contentId)"
        />
      </div>
    </div>

    <series-nav-header @save="saveSeries" @change-status="patchSeriesStatus" @delete="deleteSeries"/>
    <series-selector-content v-if="selecting" @close="selecting = false" @select="selectContent"/>
  </div>
</template>

<script>
  import {mapGetters} from "vuex"
  import TextAuto from "../../../../components/utils/TextAuto";
  import SeriesNavHeader from "../../../../components/creator/series/SeriesNavHeader";
  import SeriesSelectorContent from "../../../../components/creator/series/SeriesSelectorContent";
  import ContentInSeries from "../../../../components/creator/series/ContentInSeries";

  export default {
    components: {ContentInSeries, SeriesSelectorContent, SeriesNavHeader, TextAuto},
    layout: 'creator',
    head() {
      const title = this.series && this.series.title || 'Series'
      return {title: `Editing ${title} · ${this.creatorName}`}
    },
    computed: {
      ...mapGetters('creator', ['creatorName']),
      creatorId() {
        return this.series && this.series.creatorId || this.$store.getters['creator/creatorId']
      },
      seriesId() {
        return this.series && this.series.seriesId
      }
    },
    data() {
      return {
        selecting: false,
        contents: null,
        next: null
      }
    },
    asyncData({$api, params: {seriesId}, $error}) {
      if (seriesId === 'new') {
        return {
          started: false,
          savedTime: 0,
          changedTime: 0,
          series: {
            title: '',
            body: '',
            subtitle: '',
            image: null,
            tags: [],
            status: 'draft'
          },
        }
      }

      return $api.get(`/creators/_/series/${seriesId}`)
        .then(({data}) => ({series: data, started: true}))
        .catch((err) => $error(err))
    },
    mounted() {
      this.$$autoUpdate = setInterval(this.saveSeries, 10000)
      window.onbeforeunload = this.onBeforeUnload

      this.loadContents()
    },
    beforeDestroy() {
      clearInterval(this.$$autoUpdate)
      window.onbeforeunload = undefined
    },
    methods: {
      onBeforeUnload() {
        if (!this.started) return
        if (this.changedTime === this.savedTime) return

        return 'You have unsaved changes!'
      },
      saveSeries() {
        if (!this.started) return
        if (this.changedTime === this.savedTime) return Promise.resolve()
        const series = this.series
        const time = this.changedTime

        console.log("Saving")
        if (this.seriesId) {
          return this.$api.patch(`/creators/${this.creatorId}/series/${this.seriesId}`, series)
            .then(({data}) => {
              this.series = data
              this.savedTime = time
            })
            .catch((err) => this.$store.dispatch('addError', err))
        } else {
          return this.$api.post(`/creators/${this.creatorId}/series`, series)
            .then(({data}) => {
              this.series = data
              this.savedTime = time

              const url = `/creator/series/${data.seriesId}`
              window.history.replaceState({}, document.title, url);
            })
            .catch((err) => this.$store.dispatch('addError', err))
        }
      },
      deleteSeries() {
        if (!this.seriesId) return

        return this.$api.delete(`/creators/${this.creatorId}/series/${this.seriesId}`).then(() => {
          this.$router.push({path: '/creator/series'})
        }).catch((err) => this.$store.dispatch('addError', err))
        // TODO
      },
      patchSeriesStatus(status) {
        this.series.status = status

        return this.$api.patch(`/creators/${this.creatorId}/series/${this.seriesId}`, {status})
          .catch((err) => this.$store.dispatch('addError', err))
      },
      selectContent(content) {
        this.selecting = false
        return this.$api.post(`/creators/${this.creatorId}/series/${this.seriesId}/contents/${content.contentId}`)
          .then(() => {
            this.reloadContents()
          })
          .catch((err) => this.$store.dispatch('addError', err))
      },
      patchContent(contentId, sortId) {
        const body = {sortId}

        return this.$api.patch(`/creators/${this.creatorId}/series/${this.seriesId}/contents/${contentId}`, body)
          .then(() => {
            this.reloadContents()
          })
          .catch((err) => this.$store.dispatch('addError', err))
      },
      deleteContent(contentId) {
        return this.$api.delete(`/creators/${this.creatorId}/series/${this.seriesId}/contents/${contentId}`)
          .then(() => {
            this.reloadContents()
          })
          .catch((err) => this.$store.dispatch('addError', err))
      },
      reloadContents() {
        this.contents = null
        this.next = null
        this.loadContents()
      },
      loadContents() {
        if (!(!this.contents || this.next)) return

        const params = {index: 'sortId', size: 50}
        if (this.next) {
          params['next.sortId'] = this.next.sortId
        }

        return this.$api.get(`/creators/${this.creatorId}/series/${this.seriesId}/contents`, {params})
          .then(({data, next}) => {
            if (this.contents === null) this.contents = []
            this.contents.push(...data)
            this.next = next

            return this.loadContents()
          })
          .catch((err) => this.$store.dispatch('addError', err))
      }
    },
    watch: {
      series: {
        handler() {
          this.started = true
          this.changedTime = new Date().getTime()
        },
        deep: true
      }
    }
  }
</script>

<style scoped lang="less">
  .ContentInSeries {
    margin: -12px;

    > div {
      flex: 0 0 100%;
      max-width: 100%;

      @media (min-width: 768px) {
        flex: 0 0 50%;
        max-width: 50%;
      }

      @media (min-width: 992px) {
        flex: 0 0 33.33333%;
        max-width: 33.33333%;
      }
    }
  }
</style>
