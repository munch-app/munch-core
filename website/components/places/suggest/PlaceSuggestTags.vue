<template>
  <div class="input-text">
    <label>{{label}}</label>
    <no-ssr>
      <vue-tags-input placeholder="Add Tag" :tags="value" v-model="tag" @tags-changed="newTags => onChange(newTags)"/>
    </no-ssr>
  </div>
</template>

<script>

  let components = {};

  if (process.client) {
    components = {'VueTagsInput': require("@johmun/vue-tags-input").default};
  }

  export default {
    name: "PlaceSuggestTags",
    components: components,
    data() {
      return {
        tag: ""
      }
    },
    props: {
      label: {
        type: String,
        required: true
      },
      value: {
        type: [Array],
        required: true
      },
    },
    methods: {
      onChange(newTags) {
        this.$emit('input', newTags)
      }
    }
  }
</script>

<style lang="less">
  @Whisper100: #F0F0F8;
  @Close: #EC152C;

  .vue-tags-input {
    background: white;
    max-width: 100% !important;
  }

  .vue-tags-input {
    background: transparent;
    color: #b7c4c9;
  }

  .vue-tags-input {
    .ti-input {
      .ti-tags {
        .ti-tag.ti-valid.ti-deletion-mark {
          background-color: @Close;
          color: white;
        }

        .ti-tag.ti-valid {
          background-color: @Whisper100;
          color: black;
          font-size: 16px;
          padding-left: 9px;
          border-radius: 3px;
        }
      }
    }
  }
</style>
