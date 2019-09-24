<template>
  <div>
    <div class="flex-wrap m--4">
      <div class="p-4" v-for="(tag, index) in editing" :key="tag.id">
        <div class="border-white bg-white border-2 overflow-hidden">
          <div class="p-4-8 hover-bg-a10 hover-pointer flex-align-center" @click="onRemove(index)">
            <div class="small-bold">{{tag.name}}</div>
            <div class="ml-4">
              <simple-svg class="wh-16px" fill="black" :filepath="require('~/assets/icon/icons8-multiply.svg')"/>
            </div>
          </div>
        </div>
      </div>

      <div class="p-4" v-if="editing.length < max">
        <div class="border-blue bg-white border-2 overflow-hidden" @click="onAddDialog">
          <div class="p-4-8 hover-bg-a10 hover-pointer flex-align-center">
            <div class="small-bold blue">Add Tag</div>
            <div class="ml-4">
              <simple-svg class="wh-16px" fill="#07F" :filepath="require('~/assets/icon/icons8-add.svg')"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ArticlePlaceEditorTags",
    props: {
      value: Array,
      max: {
        type: Number,
        default: 12
      }
    },
    data() {
      if (this.value) {
        return {editing: JSON.parse(JSON.stringify(this.value)),}
      }

      return {editing: []}
    },
    mounted() {
      this.update()
    },
    methods: {
      onRemove(index) {
        this.editing.splice(index, 1)
        this.update()
      },
      onAddDialog() {
        this.$store.commit('global/setDialog', {
          name: 'SearchTagDialog', props: {
            onTag: (tag) => {
              if (_.some(this.editing, t => t.id === tag.id)) {
                return
              }

              this.editing.push(tag)
              this.update()
            }
          }
        })
      },
      update() {
        this.$emit('input', this.editing)
      },
    }
  }
</script>
