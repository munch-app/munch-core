<template>
  <div class="container-1200 mtb-32">
    <div class="flex-between">
      <h2>New Story:</h2>
      <button class="secondary-outline small" @click="onCreate">
        {{submitting === 'pending' ? 'Creating...' : 'Create'}}
      </button>
    </div>

    <div class="from-group mtb-16">
      <div v-if="submitting === 'error'">
        <p class="error" v-if="!$v.title.required">Title is required</p>
        <p class="error" v-if="!$v.title.minLength">
          Title must have at least {{$v.title.$params.minLength.min}} letters.
        </p>
        <p class="error" v-if="!$v.title.maxLength">
          Title must have lesser than {{$v.title.$params.maxLength.max}} letters.
        </p>
      </div>
      <text-auto class="Title w-100" v-model.trim="$v.title.$model" placeholder="Title">
      </text-auto>
    </div>

    <div class="mt-32">
      <div class="TypeList flex-wrap mtb-16">
        <div class="hover-pointer weight-600 border-3 border" v-for="t in types" :key="t.type"
             :class="{'bg-s500 white': selectedType.type === t.type}"
             @click="type = t.type">
          {{t.name}}
        </div>
      </div>
      <p class="mb-8 mt-16">
        {{selectedType.description}}
      </p>
      <ul>
        <li v-for="point in selectedType.points" :key="point">{{point}}</li>
      </ul>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import InputText from "../../../components/core/InputText";

  import {validationMixin} from 'vuelidate'
  import {required, minLength, maxLength} from 'vuelidate/lib/validators'
  import TextAuto from "../../../components/core/TextAuto";

  export default {
    layout: 'creator',
    components: {TextAuto, InputText},
    mixins: [validationMixin],
    head() {
      return {title: `New Story · ${this.creatorName}`}
    },
    data() {
      return {
        title: '',
        type: 'guide',
        types: [
          {
            name: 'Guide',
            type: 'guide',
            description: 'Blog content with the ability to add places. You cannot add rich html content.',
            points: ['You can add places.', 'You can add paragraphs and headings.', 'You can add rich media.'],
          },
          {
            name: 'Award',
            type: 'award',
            description: 'A collection of awarded places. This is a specialise creator list that store awards bi-directionally.',
            points: ['You can add places.', 'Award will show up on restaurant page.'],
          },
          {
            name: 'Blog',
            type: 'blog',
            description: 'Blog content with the ability to add places. Support html content.',
            points: ['You can add places.', 'You can add paragraphs and headings.', 'You can add rich media.'],
          },
        ],
        submitting: '',
      }
    },
    computed: {
      ...mapGetters('creator', ['creatorName', 'creatorId']),
      selectedType() {
        for (const argument of this.types) {
          if (argument.type === this.type) return argument
        }
      }
    },
    validations: {
      title: {
        required,
        minLength: minLength(3),
        maxLength: maxLength(250)
      },
    },
    methods: {
      onCreate() {
        if (this.submitting === 'pending') return
        this.$v.$touch()

        if (this.$v.$invalid) {
          this.submitting = 'error'
        } else {
          this.submitting = 'pending'

          this.$api.post(`/creators/${this.creatorId}/stories`, {
            title: this.title,
            type: this.type
          }).then(({data}) => {
            return this.$store.dispatch('creator/story/start', data).then(() => {
              this.$router.push({path: `/creator/stories/${data.storyId}`})
            })
          }).catch(error => {
            return this.$store.dispatch('addError', error)
          })
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Title {
    font-size: 32px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.75);

    border: none;

    &:focus {
      outline: none;
      color: rgba(0, 0, 0, 0.75);
    }

    &::placeholder {
      color: rgba(0, 0, 0, 0.5);
    }
  }

  .TypeList {
    margin: -8px;

    div {
      line-height: 1;
      padding: 10px 18px;
      margin: 8px;
    }
  }
</style>
