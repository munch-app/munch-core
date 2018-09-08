<template>
  <div class="SearchBar NoSelect">
    <input ref="input" class="TextBar Elevation1" type="text" :placeholder="placeholder" v-model="text"
           @keyup="onKeyUp" @focus="onFocus" @blur="onBlur" :class="{'Extended': extended}">
    <div class="Clear" :style="clearStyle" @click="onClear">
      <simple-svg fill="black" filepath="/img/search/close.svg"/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchBar",
    components: {},
    props: {
      placeholder: {
        type: String,
        required: false,
        default: () => 'Search e.g. Italian in Marina Bay'
      },
      extended: {
        type: Boolean,
        required: true
      }
    },
    data() {
      return {
        text: this.$route.query.q || ""
      }
    },
    mounted() {
      this.$emit('onText', this.text)
    },
    computed: {
      clearStyle() {
        return {
          'display': this.text.length > 0 ? 'block' : 'none'
        }
      }
    },
    methods: {
      onKeyUp() {
        this.$emit('onText', this.text)
      },
      onFocus() {
        this.$emit('onFocus', this.text)
      },
      onBlur() {
        this.$emit('onBlur', this.text)
      },
      onClear() {
        console.log('clear')
        this.text = ''
        this.$emit('onText', this.text)
      },
      blur() {
        this.$refs.input.blur()
      }
    }
  }
</script>

<style scoped lang="less">
  .SearchBar {
    position: relative;
    width: 100%;
    height: 40px;
  }

  .TextBar {
    border-radius: 3px;
    position: absolute;
    background-color: #FFFFFF;
    border: none transparent;
    width: 100%;
    font-size: 17px;
    height: 40px;
    padding: 0 16px;

    color: rgba(0, 0, 0, 0.6);

    &:focus {
      outline: none;
      color: black;
    }

    &.Extended {
      border-radius: 3px;

      @media (min-width: 768px) {
        border-radius: 3px 3px 0 0;
      }
    }
  }

  .Clear {
    position: absolute;
    right: 0;
    width: 40px;
    height: 40px;
    padding: 10px;

    &:hover {
      cursor: pointer;
    }
  }
</style>
