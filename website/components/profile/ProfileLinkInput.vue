<template>
  <div class="flex-wrap p-6 border-2 border">
    <div class="p-6" v-for="(link, index) in editing.links" :key="index">
      <div class="Link bg-steam border-3 p-4-8 flex-align-center">
        <div class="hover-pointer" v-if="index === focused.index">
          <div @click="onFocused(index, 'name')" v-if="focused.mode === 'url'">
            <simple-svg class="wh-16px" :filepath="require('~/assets/icon/icons8-link.svg')"/>
          </div>
          <div @click="onFocused(index, 'url')" v-if="focused.mode === 'name'">
            <simple-svg class="wh-16px" :filepath="require('~/assets/icon/icons8-text.svg')"/>
          </div>
        </div>
        <div class="hover-pointer p-2" v-else @click="onFocused(index, 'name')">
          <simple-svg class="wh-16px" :filepath="require('~/assets/icon/icons8-web.svg')"/>
        </div>

        <div v-if="index === focused.index">
          <div v-if="focused.mode === 'name'" class="p-4">
            <input class="regular" v-model="link.name" v-on:keyup.enter="onFocused(-1)" @keyup="onEmit">
          </div>
          <div v-if="focused.mode === 'url'" class="p-4">
            <input class="regular" v-model="link.url" v-on:keyup.enter="onFocused(-1)" @keyup="onEmit">
          </div>
        </div>

        <div v-else @click="onFocused(index, 'name')">
          <div class="p-4">{{link.name}}</div>
        </div>

        <div class="hover-pointer" @click="onRemove(index)">
          <simple-svg class="wh-16px" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>
    </div>

    <div class="p-6" v-if="editing.links.length < maxSize">
      <input class="New regular border border-3" placeholder="Your link (https://...)"
             v-model="editing.input" v-on:keyup.enter="onInput"
      >
    </div>
  </div>
</template>

<script>
  export default {
    name: "ProfileLinkInput",
    props: {
      value: Array
    },
    data() {
      return {
        maxSize: 4,
        editing: {
          links: this.value || [],
          input: '',
        },
        focused: {
          index: -1,
          mode: 'name' // 'name' or 'url'
        }
      }
    },
    methods: {
      onFocused(index, mode) {
        this.focused.index = index
        this.focused.mode = mode
        this.onEmit()
      },
      onRemove(index) {
        this.editing.links.splice(index, 1)
        this.onEmit()
      },
      onInput() {
        const input = this.editing.input
        this.editing.links.push({
          name: 'Link',
          url: input
        })
        this.editing.input = ''
        this.onEmit()
      },
      onEmit() {
        this.$emit('input', this.editing.links)
      }
    }
  }
</script>

<style scoped lang="less">
  input {
    outline: none;
  }

  .Link {
    input {
      border: none;
      width: 180px;
      height: 20px;
      font-size: 15px;
      padding: 0 4px;
    }
  }

  input.New {
    width: 180px;
    padding: 0 12px 0 12px;

    height: 36px;
  }
</style>
