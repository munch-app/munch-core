<template>
  <textarea
    rows="1"
    @focus="resize"
    v-model="val"
    :style="computedStyles"
  ></textarea>
</template>
<script>
  export default {
    name: 'TextAuto',
    created() {
      this.updateVal() // fill val with initial value passed via the same name prop
    },
    mounted() {
      this.resize() // perform initial height adjustment
    },
    props: {
      value: {
        type: [String],
        default: ''
      },
      minHeight: {
        type: [Number],
        'default': null
      },
      maxHeight: {
        type: [Number],
        'default': null
      },
      /*
       * Force !important for style properties
       */
      important: {
        type: [Boolean, Array],
        default: false
      }
    },
    data() {
      return {
        // data property for v-model binding with real textarea tag
        val: null,
        // works when content height becomes more then value of the maxHeight property
        maxHeightScroll: false
      }
    },
    computed: {
      computedStyles() {
        let objStyles = {}
        objStyles.resize = !this.isResizeImportant ? 'none' : 'none !important'
        if (!this.maxHeightScroll) {
          objStyles.overflow = !this.isOverflowImportant ? 'hidden' : 'hidden !important'
        }
        return objStyles
      },
      isResizeImportant() {
        const imp = this.important
        return imp === true || (Array.isArray(imp) && imp.includes('resize'))
      },
      isOverflowImportant() {
        const imp = this.important
        return imp === true || (Array.isArray(imp) && imp.includes('overflow'))
      },
      isHeightImportant() {
        const imp = this.important
        return imp === true || (Array.isArray(imp) && imp.includes('height'))
      }
    },
    methods: {
      updateVal() {
        this.val = this.value
      },
      resize: function () {
        const important = this.isHeightImportant ? 'important' : ''
        this.$el.style.setProperty('height', 'auto', important)
        let contentHeight = this.$el.scrollHeight + 1
        if (this.minHeight) {
          contentHeight = contentHeight < this.minHeight ? this.minHeight : contentHeight
        }
        if (this.maxHeight) {
          if (contentHeight > this.maxHeight) {
            contentHeight = this.maxHeight
            this.maxHeightScroll = true
          } else {
            this.maxHeightScroll = false
          }
        }
        const heightVal = contentHeight + 'px'
        this.$el.style.setProperty('height', heightVal, important)
        return this
      }
    },
    watch: {
      value() {
        this.updateVal()
      },
      val(val) {
        this.$nextTick(this.resize)
        this.$emit('input', val)
      }
    }
  }
</script>
