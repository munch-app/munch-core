<template>
  <div class="CreatorItem mtb-24 flex-row">
    <div class="flex-align-center mr-16">
      <simple-svg @click.native="onMoveUp" class="Icon hover-pointer wh-24px" fill="black" :filepath="require('~/assets/icon/creator/up.svg')"/>
      <simple-svg @click.native="onMoveDown" class="Icon hover-pointer wh-24px" fill="black" :filepath="require('~/assets/icon/creator/down.svg')"/>
    </div>

    <div class="flex-grow">
      <creator-item-text v-if="isText" :item="item" @change="onChange"/>
      <creator-item-line v-else-if="item.type === 'line'"/>
      <creator-item-html v-else-if="item.type === 'html'" :item="item" @change="onChange"/>
      <creator-item-place v-else-if="item.type === 'munchPlace'" :item="item" @change="onChange"/>
      <creator-item-image v-else-if="item.type === 'image'" :item="item" @change="onChange"/>
      <p v-else>{{item}}</p>
    </div>

    <div class="flex-align-center flex-justify-end ml-16" @click="onDelete">
      <simple-svg class="Icon hover-pointer wh-24px" fill="black" :filepath="require('~/assets/icon/creator/trash.svg')"/>
    </div>
  </div>
</template>

<script>
  import CreatorItemText from "./CreatorItemText";
  import CreatorItemLine from "./CreatorItemLine";
  import CreatorItemHtml from "./CreatorItemHtml";
  import CreatorItemPlace from "./CreatorItemPlace";
  import CreatorItemImage from "./CreatorItemImage";

  export default {
    name: "CreatorItem",
    components: {CreatorItemImage, CreatorItemPlace, CreatorItemHtml, CreatorItemLine, CreatorItemText},
    props: {
      item: {
        type: Object,
        required: true,
        twoWay: true,
      }
    },
    computed: {
      isText() {
        switch (this.item.type) {
          case 'body':
          case 'quote':
          case 'heading1':
          case 'heading2':
            return true

          default:
            return false
        }
      }
    },
    methods: {
      onMoveUp() {
        this.$emit('move-up', this.item)
      },
      onMoveDown() {
        this.$emit('move-down', this.item)
      },
      onDelete() {
        this.$emit('delete', this.item)
      },
      onChange() {
        this.$emit('change', this.item)
      }
    }
  }
</script>

<style scoped lang="less">
  .CreatorItem {
    margin-left: -64px;
    margin-right: -40px;

    .Icon {
      opacity: 0;
    }

    &:hover {
      .Icon {
        opacity: 1;
      }
    }
  }
</style>
