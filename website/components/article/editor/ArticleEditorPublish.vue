<template>
  <div class="bg-white wh-100 flex-center">
    <div class="container-1200 flex-1-2">
      <div class="p-24">
        <h4>Article Preview</h4>
        <div class="Image mt-16" @click="onImage">
          <cdn-img v-if="value.image" :image="value.image" class="wh-100">
            <div class="flex-center">
              <button class="border small">Change Article Image</button>
            </div>
          </cdn-img>
          <div v-else class="bg-steam flex-center wh-100">
            <button class="border small">Change Article Image</button>
          </div>
        </div>
        <div class="pt-24 hr-bot">
          <text-auto class="mb-4 h3" :value="value.title" @input="onInputTitle"
                     placeholder="Title"/>
          <div class="tiny b-a75 mb-8" :class="{error: state === 'error'}">{{(value.title || "").length}}/100 (required)</div>
        </div>
        <div class="pt-24 hr-bot">
          <text-auto class="mb-4 regular" :value="value.description" @input="onInputDescription"
                     placeholder="Description"/>
          <div class="tiny b-a75 mb-8" :class="{error: state === 'error'}">{{(value.description || "").length}}/250 (required)</div>
        </div>

        <div class="ptb-16">
          <div class="small">
            <b>Note</b>: Changes made here only appears in SEO and summarised information — not the article itself.
          </div>
        </div>
      </div>

      <div class="p-24">
        <div>
          <div class="ptb-8">
            <h4>
              <span class="small"><b>Tags</b>: You can add up to 8 tags.</span>
            </h4>

            <article-editor-tag-input class="mt-8" :tags="value.tags" @on-add="onTagAdd" @on-remove="onTagRemove"/>
          </div>
          <div v-if="hasPlaces" class="flex-align-center hover-pointer mt-16" @click="onInputMap">
            <div class="checkbox" :class="{selected: value.options.map}"/>
            <small class="ml-16">Highlight places on a embedded map?</small>
          </div>
          <div v-if="hasPlaces" class="flex-align-center hover-pointer mt-16" @click="onInputPublishing">
            <div class="checkbox" :class="{selected: value.options.placePublishing}"/>
            <small class="ml-16">Keep your places information updated?</small>
          </div>
        </div>
        <div class="mt-24">
          <button class="pink" @click="onPublish">Publish now</button>
          <button class="ml-16 border" @click="onCancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import TextAuto from "../../utils/TextAuto";
  import CdnImg from "../../utils/image/CdnImg";
  import ImageUploadDialog from "../../dialog/ImageUploadDialog";
  import ArticleEditorTagInput from "./ArticleEditorTagInput";

  export default {
    name: "ArticleEditorPublish",
    components: {ArticleEditorTagInput, ImageUploadDialog, CdnImg, TextAuto},
    props: {
      value: Object
    },
    computed: {
      hasPlaces() {
        return this.value.content.some(s => s.type === 'place')
      }
    },
    data() {
      return {
        state: null
      }
    },
    methods: {
      onTagAdd(tag) {
        let tags = [...this.value.tags, tag]
        tags = _.uniqBy(tags, function (e) {
          return e.id;
        });
        this.$emit('input', {...this.value, tags})
      },
      onTagRemove(tag) {
        const tags = this.value.tags.filter(t => t.id !== tag.id)
        this.$emit('input', {...this.value, tags})
      },
      onInputTitle(title) {
        this.$emit('input', {...this.value, title: title.substring(0, 100)})
      },
      onInputDescription(description) {
        this.$emit('input', {...this.value, description: description.substring(0, 250)})
      },
      onImage() {
        this.$store.commit('global/setDialog', {
          name: 'ImageUploadDialog', props: {
            onImage: (image) => {
              this.$store.commit('global/clearDialog');
              this.$emit('input', {...this.value, image})
              this.$emit('on-save')
            }
          }
        })
      },
      onInputMap() {
        this.$emit('input', {...this.value, options: {...this.value.options, map: !this.value.options.map}})
      },
      onInputPublishing() {
        this.$emit('input', {
          ...this.value,
          options: {...this.value.options, placePublishing: !this.value.options.placePublishing}
        })
      },
      onPublish() {
        if (this.value.title === '' || this.value.description === '') {
          this.$store.dispatch('addMessage', {title: 'Missing info', message: 'Title and description is required.'})
          this.state = 'error'
        } else {
          this.$emit('on-publish')
        }
      },
      onCancel() {
        this.$emit('on-save')
        this.$emit('on-cancel')
      },
    },
  }
</script>

<style scoped lang="less">
  .Image {
    height: 120px;
    width: 100%;
  }
</style>
