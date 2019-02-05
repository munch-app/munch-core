<template>
  <div class="container-1200 mtb-32">
    <text-auto class="h1 Input" v-model.trim="story.title" placeholder="Title"/>

  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import TextAuto from "../../../components/core/TextAuto";

  import {validationMixin} from 'vuelidate'
  import {required, minLength, maxLength} from 'vuelidate/lib/validators'

  export default {
    layout: 'creator',
    components: {TextAuto},
    mixins: [validationMixin],
    head() {
      return {title: `${this.storyTitle || 'Story'} · ${this.creatorName}`}
    },
    asyncData({$api, store, params: {storyId}, $error}) {
      const story = store.state.creator.story.story
      if (story && story.storyId === storyId) return {story}

      return $api.get(`/creators/_/stories/${storyId}`).then(({data}) => {
        return store.dispatch('creator/story/start', data).then((story) => {
          return {story}
        })
      }).catch((err) => $error(err))
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
      ...mapGetters('creator/story', ['storyId', 'storyTitle']),
    },
  }
</script>

<style scoped lang="less">
  .Input {
    width: 100%;
    border: none;
    color: rgba(0, 0, 0, 0.75);

    &:focus {
      outline: none;
      color: rgba(0, 0, 0, 0.75);
    }

    &::placeholder {
      color: rgba(0, 0, 0, 0.6);
    }
  }
</style>
