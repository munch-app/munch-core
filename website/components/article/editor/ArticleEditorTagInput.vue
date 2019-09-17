<template>
  <div class="TagInput flex-wrap bg-steam p-12">
    <div class="Spacer" v-for="tag in tags" :key="tag.id">
      <div class="Tag bg-white border border-3 flex-align-center hover-pointer" @click="onTagRemove(tag)">
        <div class="mr-4">
          <small>{{tag.name}}</small>
        </div>
        <div class="wh-16px">
          <simple-svg class="w-100" fill="#444" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>
    </div>
    <div class="relative Search" v-if="tags.length < 8">
      <div class="Spacer">
        <input class="wh-100 small" placeholder="Search tags" v-model="query">
        <div class="List border absolute bg-white index-1">
          <div class="Item hover-pointer hover-bg-a10" v-for="tag in list" :key="tag.id" @click="onTagAdd(tag)">
            <small>{{tag.name}}</small>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "ArticleEditorTagInput",
    props: {
      tags: Array
    },
    data() {
      return {
        query: ""
      }
    },
    methods: {
      onTagAdd(tag) {
        this.query = ''
        this.list = []

        if (_.some(this.tags, t => t.id === tag.id)) {
          return
        }

        this.$emit('on-add', tag)
      },
      onTagRemove(tag) {
        this.$emit('on-remove', tag)
      }
    },
    subscriptions() {
      return {
        list: this.$watchAsObservable('query').pipe(
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
  .Spacer {
    padding: 4px;
  }

  .TagInput {
    min-height: 56px;

    input {
      outline: none;
      border: none;
      width: 150px;
      padding: 0 12px 0 12px;
      height: 32px;
    }
  }

  .Tag {
    padding: 0 8px 0 12px;
    height: 32px;

    small {
      line-height: 32px;
    }
  }

  .Search .List {
    min-width: 150px;
  }

  .Item {
    padding: 5px 12px;
  }
</style>
