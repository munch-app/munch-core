<template>
  <div class="Avatar bg-steam border-3 flex-align-center p-12" :class="{Editing: editing}">
    <div class="flex-no-shrink">
      <div class="wh-64px border-circle overflow-hidden bg-white" @click="onImage">
        <cdn-img v-if="image" :image="image">
          <div v-if="editing" class="p-12" :class="{'hover-bg-a40 hover-pointer': editing}">
            <simple-svg fill="#fff" :filepath="require('~/assets/icon/icons8-person.svg')"/>
          </div>
        </cdn-img>
        <div v-else class="p-12" :class="{'hover-bg-a40 hover-pointer': editing}">
          <simple-svg fill="#000" :filepath="require('~/assets/icon/icons8-person.svg')"/>
        </div>
      </div>
    </div>

    <div class="ml-16 flex-column flex-grow relative">
      <template v-if="editing">
        <input class="clear h4" v-model="line1" placeholder="Title"/>
        <input class="clear regular b-a60" v-model="line2" placeholder="Subtitle"/>

        <div class="absolute position-tb-0 position-r-0 flex-column-justify-end flex-end">
          <div class="p-4-12 bg-white border-error border-2" v-if="requiredFields.length">
            <div class="error small-bold">Missing: {{requiredFields.join(', ')}}</div>
          </div>
        </div>
      </template>
      <template v-else>
        <h4>{{line1}}</h4>
        <div class="regular b-a60">{{line2}}</div>
      </template>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../../utils/image/CdnImg";
  import ImageUploadDialog from "../../dialog/ImageUploadDialog";

  export default {
    name: "ArticleAvatar",
    components: {ImageUploadDialog, CdnImg},
    props: ['node', 'updateAttrs', 'editable'],
    computed: {
      editing() {
        return !!this.updateAttrs
      },
      image() {
        return this.images[0]
      },
      images: {
        get() {
          return this.node.attrs.images || []
        },
        set(images) {
          this.updateAttrs({
            images,
          })
        },
      },
      line1: {
        get() {
          return this.node.attrs.line1
        },
        set(line1) {
          this.updateAttrs({
            line1,
          })
        },
      },
      line2: {
        get() {
          return this.node.attrs.line2
        },
        set(line2) {
          this.updateAttrs({
            line2,
          })
        },
      },
      requiredFields() {
        const fields = []
        if(!this.line1) fields.push('Title')
        if(!this.line2) fields.push('Subtitle')
        return fields
      }
    },
    data() {
      return {
        state: null
      }
    },
    methods: {
      onImage() {
        if (!this.editing) {
          return
        }

        this.$store.commit('global/setDialog', {
          name: 'ImageUploadDialog', props: {
            onImage: (image) => {
              this.$store.commit('global/clearDialog');
              this.images = [image]
            }
          }
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .Avatar.Editing {
    &:hover {
      outline: 3px solid #07F;
    }
  }

  input {
    width: 100%;
    padding: 2px 6px;
    margin: -2px -6px;
    border-radius: 2px;

    &:hover, &:focus {
      background: #FFF;
    }
  }
</style>
