<template>
  <div class="Border" :class="{Fixed: fixed}">
    <div class="Field index-navigation hr-bot">
      <input class="h3 clear w-100 p-16" ref="input" v-model="query" :placeholder="inputHint">
    </div>

    <div class="flex-column">
      <div v-if="documents">
        <div class="hr-bot hover-pointer" v-for="document in documents" :key="document.key" @click="onSelect(document)">
          <slot v-bind:document="document"></slot>
        </div>
      </div>

      <div v-if="documents">
        <div v-if="create && query" @click="onCreate({name: query, location: {}})" class="flex-grow flex-self-stretch">
          <slot name="create">
            <div class="p-16 hr-bot flex-column-align-center hover-bg-a10 hover-pointer h-100">
              <h5 class="mb-4"><span class="weight-400">Can't find</span> {{query}}?</h5>
              <button class="pink-outline tiny">Create a new place?</button>
            </div>
          </slot>
        </div>

        <div v-if="hasMore" @click="onAppend" class="flex-self-stretch">
          <slot name="loadMore">
            <div class="p-16 hr-top flex-justify-center flex-align-center hover-bg-a10 hover-pointer">
              <simple-svg class="wh-32px" :filepath="require('~/assets/icon/icons8-scroll-down.svg')"/>
              <h5 class="ml-8">Load More</h5>
            </div>
          </slot>
        </div>
      </div>

      <div v-if="state === 'loading'" class="flex-center p-16">
        <beat-loader color="#07F" size="12px"/>
      </div>
    </div>
  </div>
</template>

<script>
  import {filter, pluck, tap, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'
  import CdnImg from "../utils/image/CdnImg";

  export default {
    name: "SearchPlace",
    components: {CdnImg},
    props: {
      create: {
        type: Boolean,
        default: false
      },
      autoFocus: {
        type: Boolean,
        default: false
      },
      size: {
        type: Number,
        default: 20
      },
      fields: {
        type: String,
        default: 'key,id,name,image,location,tags,status'
      },
      inputHint: {
        type: String,
        default: 'Search restaurant'
      },
      preFill: {
        type: String,
      },
      fixed: {
        type: Boolean
      }
    },
    data() {
      return {
        query: this.preFill || '',
        state: '',

        documents: null,
        from: 0,
      }
    },
    computed: {
      hasMore() {
        if (this.documents?.length === 0) return false

        return this.documents?.length % this.size === 0
      }
    },
    mounted() {
      if (this.autoFocus) {
        this.$nextTick(() => {
          this.$refs.input.focus()
        })
      }

      if (this.preFill) {
        this.state = 'loading'
        this.$api.get('/places/search', {
          params: {
            text: this.preFill, fields: this.fields, size: this.size
          }
        }).then(({data}) => {
          this.state = null
          this.documents = data
        })
      }
    },
    methods: {
      onSelect(document) {
        this.$emit('on-select', document)
      },
      onCreate(document) {
        this.$emit('on-create', document)
      },
      onAppend() {
        this.from += this.size
        this.state = 'loading'

        return this.$api.get('/places/search', {
          params: {
            text: this.query, fields: this.fields, from: this.from, size: this.size
          }
        }, {progress: false}).then(({data}) => {
          this.documents.push(...data)
          this.state = null
        })
      }
    },
    subscriptions() {
      const observable = this.$watchAsObservable('query').pipe(
        pluck('newValue'),
        tap(() => {
          this.from = 0
          this.documents = null
        }),
        map((text) => text.trim()),
        filter((text) => text.length > 1),
        tap(() => {
          this.state = 'loading'
        }),
        distinctUntilChanged(),
        debounceTime(333),
      )

      return {
        documents: observable.pipe(
          switchMap((text) => {
            return this.$api.get('/places/search', {
              params: {
                text, fields: this.fields, size: this.size
              }
            }, {progress: false})
          }),
          map(({data: documents}) => documents),
          tap(() => {
            this.state = null
          })
        )
      }
    }
  }
</script>

<style scoped lang="less">
  input {
    &:focus {
      background: #FAFAFA;
    }

    border: 3px solid #EFEFEF;
  }

  .Fixed {
    .Field {
      position: sticky;
      top: 0;
    }
  }
</style>
