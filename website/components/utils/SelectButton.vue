<template>
  <div class="SelectButton relative" :class="{Focused: focused, Default: _isDefault}" v-on-clickaway="onClose">
    <div @click="onToggle"
         class="Button overflow-hidden hover-pointer elevation-hover-2 flex-align-center flex-between bg-white">
      <h5 class="lh-1 black-a70">
        {{_label}}
      </h5>
      <div class="ml-8">
        <simple-svg class="wh-20px" v-if="_isDefault" fill="rgba(0,0,0,0.66)" :filepath="require('~/assets/icon/icons8-sort-down.svg')"/>
        <simple-svg class="wh-20px" v-else fill="#fff" :filepath="require('~/assets/icon/icons8-sort-down.svg')"/>
      </div>
    </div>

    <div class="Selections absolute position-lr-0 index-elevation overflow-hidden" v-show="focused">
      <div v-for="selection in selections" :key="selection.name">
        <div class="Selection overflow-hidden hover-pointer flex-align-center flex-between bg-white"
             @click="onClick(selection)">
          <h5 class="lh-1 black-a70">
            {{selection.name}}
          </h5>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SelectButton",
    props: {
      /**
       * Default Label
       */
      label: {
        type: String,
        required: true
      },
      /**
       * default = selected by default
       *
       * {name: 'Any', default: true},
       * {name: 'In Selection'},
       */
      selections: {
        type: Array,
        required: true
      },
      /**
       * {name: 'In Selection', default: true}
       */
      selected: {
        type: Object,
      },
    },
    data() {
      return {
        focused: false,
      }
    },
    computed: {
      _label() {
        if (this.selected?.default) {
          return this.label
        }
        return this.selected?.name || this.label
      },
      _isDefault() {
        return this.selected && this.selected.default
      }
    },
    methods: {
      onClose() {
        this.focused = false
      },
      onToggle() {
        this.focused = !this.focused
      },
      onClick(selection) {
        this.focused = false
        this.$emit('select', selection)
      }
    }
  }
</script>

<style scoped lang="less">
  @Border: 1px solid rgba(0, 0, 0, 0.66);
  @Blue: #07F;
  @Radius: 3px;

  .Selection,
  .Button {
    height: 44px;
    min-width: 160px;
    padding-left: 16px;
    padding-right: 16px;
  }

  .Button {
    border: @Border;
    border-radius: @Radius;
  }

  :not(.Default) > .Button {
    background: @Blue;
    border: white;

    h5 {
      color: white;
    }
  }

  .Focused .Button {
    border-bottom: none;
    border-radius: @Radius @Radius 0 0;
  }

  .Selections {
    border-left: @Border;
    border-right: @Border;
    border-bottom: @Border;
    border-radius: 0 0 @Radius @Radius;
  }

  .Selection {
    border-top: @Border;
  }
</style>
