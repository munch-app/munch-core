<template>
  <div class="input-text">
    <label>{{label}} <span class="error" v-if="required && value === ''">is required</span></label>

    <div class="relative">
      <input :value="value" @input="onInput($event.target.value)" :placeholder="placeholder"
             :type="type"
      >

      <div class="Clear absolute bg-white hover-pointer flex-center" @click="onInput('')">
        <simple-svg class="Icon" fill="black" filepath="/img/search/close.svg"/>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "InputText",
    props: {
      placeholder: String,
      label: {
        type: String,
        required: true
      },
      value: {
        type: [String, Number],
      },
      required: {
        type: Boolean,
        default: false
      },
      type: {
        type: String,
        default: 'text'
      }
    },
    methods: {
      onInput(value) {
        this.$emit('input', value)
      }
    }
  }
</script>

<style scoped lang="less">
  .Clear {
    top: 3px;
    bottom: 3px;
    right: 3px;

    padding-left: 5px;
    padding-right: 5px;

    .Icon {
      width: 18px;
      height: 18px;
    }
  }
</style>
