<template>
  <div class="border bg-steam border-3">
    <div v-for="(synonym, index) in editing" :key="synonym">
      <div class="flex-between">
        <div @click="onSwap(index)" class="flex-grow p-12 hover-pointer hover-bg-a10">
          {{synonym}}
        </div>

        <div @click="onRemove(index)" class="flex-self-stretch flex-center p-12 hover-pointer hover-bg-a10">
          <simple-svg class="wh-16px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
        </div>
      </div>
    </div>

    <div v-if="editing.length < max">
      <input class="p-12" v-model.trim="input" @keyup.enter="onKeyEnter" @change="update">
    </div>
  </div>
</template>

<script>
  export default {
    name: "EditorSynonyms",
    props: {
      value: Array,
      max: {
        type: Number,
        default: 4
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
    methods: {
      onSwap(index) {
        if (this.input.trim()) {
          const swapping = this.editing[index]
          this.editing[index] = this.input
          this.input = swapping
        } else {
          this.input = this.editing[index]
          this.editing.splice(index, 1)
        }
      },
      onRemove(index) {
        this.editing.splice(index, 1)

        this.update()
      },
      onKeyEnter() {
        this.editing.push(this.input)
        this.input = ''

        this.update()
      },
      update() {
        if (this.input.trim()) {
          this.$emit('input', [...this.editing, this.input])
        } else {
          this.$emit('input', this.editing)
        }
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

    border-bottom-left-radius: 3px;
    border-bottom-right-radius: 3px;
    border: 1px solid #00000000;

    &:focus {
      border: 1px solid #07F;
    }
  }
</style>
