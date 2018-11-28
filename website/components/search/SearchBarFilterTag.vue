<template>
  <div class="TagColumn">
    <div class="TagRow" v-for="tag in tags" :key="tag.tagId" @click="toggle(tag)"
         v-if="tag.count > 0 || !hidden"
         :class="{Selected: isSelectedTag(tag), Loading: loading}">

      <div class="Name">
        {{tag.name}}
      </div>

      <div class="Control">
        <div v-if="!loading" class="Count weight-600 b-a75">{{countText(tag.count)}}</div>
        <div v-if="!loading || isSelectedTag(tag)" class="Checkbox"/>
        <beat-loader v-if="!isSelectedTag(tag) && loading" class="flex-center" color="#0A6284" size="6px"/>
      </div>
    </div>

    <div class="LastRow TagRow" v-if="hiddenCount > 0" @click="hidden = !hidden">
      <div class="Name s500 weight-600">{{hidden ? 'See' : 'Hide'}} other filters</div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "SearchBarFilterTag",
    props: {
      type: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        hidden: true
      }
    },
    computed: {
      ...mapGetters('filter', ['isSelectedTag', 'loading', 'isSelectedLocationType', 'tagGraph']),

      /**
       * List of Tags {tagId, name, type, count}
       */
      tags() {
        if (!this.tagGraph) return []

        return this.tagGraph.tags
          .filter(t => t.type === this.type)
      },
      hiddenCount() {
        if (!this.tagGraph) return 0

        return this.tagGraph.tags
          .filter(t => t.type === this.type && t.count === 0)
          .length
      },
    },
    methods: {
      toggle(tag) {
        this.$store.dispatch('filter/tag', tag)
      },
      countText(count) {
        if (count) {
          if (count >= 100) return '100+'
          else if (count <= 10) return `${count}`
          else return `${Math.round(count / 10) * 10}+`
        }
        return '0'
      }
    },
  }
</script>

<style scoped lang="less">
  .TagColumn {
    overflow-y: auto;

    @media (min-width: 768px) {
      // 104 Height of nav bar
      max-height: calc(90vh - 104px - 64px);
    }
  }

  .TagRow {
    display: flex;
    padding: 8px 0;

    &:hover {
      cursor: pointer;
    }
  }

  .Name {
    padding-right: 8px;
    font-size: 16px;
    line-height: 24px;

    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    @media (min-width: 768px) {
      padding-right: 80px;
    }
  }

  .Control {
    display: flex;
    justify-content: flex-end;
    align-items: center;

    flex-grow: 1;

    .Count {
      margin-right: 16px;
    }
  }

  .Checkbox {
    height: 24px;
    width: 24px;
    position: relative;
    display: inline-block;

    &::before, &::after {
      position: absolute;
      content: "";
      display: inline-block;
    }

    &::before {
      margin: 1px 0;
      height: 22px;
      width: 22px;
      border-radius: 2px;
      border: 2px solid rgba(0, 0, 0, .75);
    }

    &::after {
      height: 7px;
      width: 11px;
      border-left: 2px solid;
      border-bottom: 2px solid;

      transform: rotate(-45deg);

      left: 5px;
      top: 7px;
    }

    &::after {
      content: none;
    }
  }

  @Primary500: #F05F3B;

  .TagRow.Selected {
    .Checkbox::after {
      content: "";
      border-color: white;
    }

    .Checkbox::before {
      border-color: @Primary500;
      background-color: @Primary500;
    }

    .Checkbox::before {
      border-color: @Primary500;
      background-color: @Primary500;
    }
  }
</style>
