<template>
  <div class="bg-white dialog-small dialog-h80vh p-0">
    <input class="h3 clear w-100 p-16" v-model="input" placeholder="Search Tag">
    <div class="w-100 bg-white index-1 overflow-y-auto">
      <div class="hover-pointer hover-bg-a10" v-for="tag in list" :key="tag.id" @click="onAdd(tag)">
        <div class="p-12-16">
          <h5>{{tag.name}}</h5>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "SearchTagDialog",
    props: {
      onTag: Function
    },
    data() {
      return {input: ''}
    },
    methods: {
      onAdd(tag) {
        this.input = ''
        this.list = []

        this.$store.commit('global/clearDialog')
        this.onTag(tag)
      },
    },
    subscriptions() {
      return {
        list: this.$watchAsObservable('input').pipe(
          pluck('newValue'),
          map((text) => text.trim()),
          filter((text) => text !== ''),
          debounceTime(200),
          distinctUntilChanged(),
          switchMap((name) => {
            return this.$api.post('/tags/search', {name}, {progress: false})
          }),
          map(({data}) => data)
        )
      }
    }
  }
</script>

<style scoped lang="less">
  input {
    outline: none;
    background: #FFF;
    color: black;

    border: 3px solid #EFEFEF;
  }
</style>
