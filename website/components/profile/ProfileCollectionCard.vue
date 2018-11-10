<template>
  <div>
    <div class="Editing" v-if="showButton">
      <div v-if="isEditable">
        <div @click="deleting = true" class="hover-pointer">
          <simple-svg class="Icon" fill="black" filepath="/img/profile/delete.svg"/>
        </div>
        <div @click="editing = true" class="hover-pointer">
          <simple-svg class="Icon" fill="black" filepath="/img/profile/edit.svg"/>
        </div>
      </div>
    </div>

    <nuxt-link class="CollectionCard hover-pointer" :to="`/collections/${collection.collectionId}`">
      <image-size class="Image" :image="collection.image">
        <div class="GreyscaleContainer">
          <div class="flex-center ImageContainer">
            <div class="heading Name white text-ellipsis-1l">{{collection.name}}</div>
          </div>
        </div>
      </image-size>
    </nuxt-link>

    <portal to="dialog-styled" v-if="deleting">
      <h3>Delete '{{data.name}}'?</h3>
      <p>You cannot recover a collection once it is deleted.</p>
      <div class="right">
        <button class="clear-elevated" @click="deleting = false">Cancel</button>
        <button class="secondary" @click="onDeleteCollection">Delete</button>
      </div>
    </portal>

    <portal to="dialog-styled" v-if="editing">
      <h3>Edit '{{data.name}}'</h3>
      <input v-model="data.name" placeholder="Name">
      <input v-model="data.description" placeholder="Description">
      <div class="right">
        <button class="clear-elevated" @click="editing = false">Cancel</button>
        <button class="secondary" @click="onEditCollection">Confirm</button>
      </div>
    </portal>
  </div>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "ProfileCollectionCard",
    components: {ImageSize},
    props: {
      collection: {
        type: Object,
        required: true
      },
      showButton: {
        type: Boolean,
        required: true
      }
    },
    data() {
      return {
        editing: false,
        deleting: false,
        data: JSON.parse(JSON.stringify(this.collection))
      }
    },
    computed: {
      isEditable() {
        return this.collection.createdBy === 'User'
      }
    },
    methods: {
      onDeleteCollection() {
        return this.$store.dispatch('user/collections/delete', this.collection.collectionId)
          .then(() => {
            this.deleting = false
          }).catch(error => {
            this.$store.dispatch('addError', error)
          })
      },
      onEditCollection() {
        return this.$store.dispatch('user/collections/patch', this.data)
          .then(() => {
            this.editing = false
          }).catch(error => {
            this.$store.dispatch('addError', error)
          })
      }
    }
  }
</script>

<style scoped lang="less">
  .CollectionCard {
    color: initial;
    text-decoration: initial;
  }

  .Image {
    width: 100%;
    border-radius: 4px;
    overflow: hidden;

    padding-top: 66%;

    .GreyscaleContainer {
      position: relative;
      padding-left: 12px;
      padding-right: 12px;
      padding-top: 45%;
      transition: all 0.3s cubic-bezier(.25, .8, .25, 1);
      background-color: rgba(0, 0, 0, 0.55);
      height: 100%;

      .Name {
        font-size: 18px;
        font-weight: 600;
      }

      &:hover {
        background-color: rgba(0, 0, 0, 0.33);
      }
    }

    .ImageContainer {
      position: absolute;
      top: 0;
      left: 12px;
      right: 12px;
      bottom: 0;

      height: 100%;
    }
  }

  .Editing {
    height: 24px;
    width: 100%;
    margin-bottom: 8px;

    > div {
      justify-content: flex-end;
      display: flex;
    }

    .Icon {
      width: 24px;
      height: 24px;
      margin-right: 4px;
    }
  }
</style>
