<template>
  <div @select="onFocus">
    <div class="absolute" v-show="focused">
      <div class="TextFloating flex bg-b85 border-4 white weight-600 p-8 regular lh-1 hover-pointer">
        <div v-for="type in types" :key="type.type" :class="{'p500': item.type === type.type}"
             @click="updateType(type.type)" >
          {{type.name}}
        </div>
      </div>
    </div>

    <text-auto ref="field" class="TextAuto" :class="item.type" :placeholder="placeholder"
               @blur="onBlur" @change="onChange" v-model="item.text"/>
  </div>
</template>

<script>
  import TextAuto from "../../core/TextAuto";

  export default {
    name: "CreatorItemText",
    components: {TextAuto},
    props: {
      item: {
        type: Object,
        required: true,
        twoWay: true,
      }
    },
    data() {
      return {
        focused: false,
        types: [
          {type: 'body', name: 'Body'},
          {type: 'heading1', name: 'H1'},
          {type: 'heading2', name: 'H2'},
        ],
      }
    },
    computed: {
      placeholder() {
        switch (this.item.type) {
          case 'body':
            return 'Text'

          case 'heading1':
            return 'Heading 1'

          case 'heading2':
            return 'Heading 2'

          case 'quote':
            return 'Quote'

          default:
            return ''
        }
      },
    },
    methods: {
      updateType(type) {
        this.item.type = type
        this.$emit('change')

        this.$nextTick(() => {
          this.$refs.field.resize()
        })
      },
      onBlur() {
        setTimeout(() => {
          this.focused = false
        }, 500)
      },
      onFocus() {
        this.focused = true
      },
      onChange() {
        this.focused = false
        this.$emit('change')
      }
    }
  }
</script>

<style scoped lang="less">
  .TextAuto {
    margin-bottom: 0;
  }

  .heading1 {
    font-weight: 600 !important;;
    font-size: 32px !important;;
    line-height: 42px !important;;
  }

  .heading2 {
    font-weight: 600 !important;;
    font-size: 24px !important;;
    line-height: 32px !important;;
  }

  .body {
    font-weight: 400 !important;
    font-size: 19px !important;;
    line-height: 1.5 !important;;
  }

  .TextFloating {
    margin-top: -40px;

    div {
      padding-left: 8px;
      padding-right: 8px;
    }
  }
</style>
