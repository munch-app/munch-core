<template>
  <div class="container-1200 mtb-48">
    <text-auto class="h1 Input" v-model.trim="story.title" placeholder="Title"/>
    <text-auto class="subtext Input" v-model.trim="story.subtitle" placeholder="Subtitle"/>
    <text-auto class="regular Input" v-model.trim="story.body" placeholder="Body"/>

  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import TextAuto from "../../../components/core/TextAuto";

  import {validationMixin} from 'vuelidate'
  import {required, minLength, maxLength} from 'vuelidate/lib/validators'

  export default {
    components: {TextAuto},
    mixins: [validationMixin],
    head() {
      return {title: `${this.storyTitle || 'Story'} · ${this.creatorName}`}
    },
    asyncData({$api, store, params: {storyId}, error}) {
      const story = store.state.creator.story.story
      if (story && story.storyId === storyId) return {story}

      return $api.get(`/creators/_/stories/${storyId}`).then(({data}) => {
        return store.dispatch('creator/story/start', data).then((story) => {
          return {story}
        })
      }).catch((err) => {
        if (err.statusCode === 404) error({statusCode: 404, message: 'Story Not Found'})
        else if (err.statusCode === 403) error({statusCode: 403, message: 'Forbidden'})
        else store.dispatch('addError', err)
      })
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
      ...mapGetters('creator/story', ['storyId', 'storyTitle']),
    },
    validations: {
      story: {
        title: {
          required,
          minLength: minLength(3),
          maxLength: maxLength(250)
        },
        subtitle: {
          minLength: minLength(3),
          maxLength: maxLength(100)
        },
        body: {
          minLength: minLength(3),
          maxLength: maxLength(3000)
        },
      },
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
