<template>
  <div class="border border-3 bg-steam p-16">
    <div v-if="editing.length" class="mb-16">
      <div class="flex-wrap m--4">
        <div class="p-4" v-for="(tag, index) in editing" :key="tag.id">
          <div class="bg-white border-2 overflow-hidden">
            <div class="p-4-8 hover-bg-a10 hover-pointer flex-align-center">
              <div class="small-bold">{{tag.name}}</div>
              <div @click="onRemove(index)" class="ml-4">
                <simple-svg class="wh-16px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="relative" v-if="editing.length < max">
      <input class="p-12" v-model="input" @keyup.enter="onKeyEnter" @change="update" placeholder="Search tags">

      <div class="w-100 hr-bot hr-left hr-right border-3 absolute bg-white index-1">
        <div class="Item hover-pointer hover-bg-a10" v-for="tag in list" :key="tag.id" @click="onAdd(tag)">
          <div class="p-12-16">{{tag.name}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {pluck, filter, debounceTime, distinctUntilChanged, switchMap, map} from 'rxjs/operators'

  export default {
    name: "EditorTags",
    props: {
      value: Array,
      max: {
        type: Number,
        default: 12
      }
    },
    data() {
      if (this.value) {
        return {
          editing: JSON.parse(JSON.stringify(this.value)),
          input: ''
        }
      }

      return {editing: [], input: ''}
    },
    mounted() {
      this.update()
    },
    methods: {
      onRemove(index) {
        this.editing.splice(index, 1)

        this.update()
      },
      onAdd(tag) {
        this.input = ''
        this.list = []

        if (_.some(this.editing, t => t.id === tag.id)) {
          return
        }

        this.editing.push(tag)
        this.update()
      },
      update() {
        this.$emit('input', this.editing)
      }
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

    width: 100%;
    font-size: 17px;

    border-radius: 3px;
    border: 1px solid #00000000;

    &:focus {
      border: 1px solid #07F;
    }
  }
</style>
