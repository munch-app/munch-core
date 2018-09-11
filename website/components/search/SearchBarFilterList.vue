<template>
  <div class="Container" v-if="selected">
    <div class="FilterListContainer">
      <div class="FilterList">
        <div class="Group Cuisine" :class="{'Selected': selected === 'cuisine'}">
          <h2 class="FilterName">Cuisine</h2>
          <search-bar-filter-tag type="cuisine"/>
        </div>

        <div class="Group Amenities" :class="{'Selected': selected === 'amenities'}">
          <h2 class="FilterName">Amenities</h2>
          <search-bar-filter-tag type="amenities"/>
        </div>

        <div class="Group Establishments" :class="{'Selected': selected === 'establishments'}">
          <h2 class="FilterName">Establishments</h2>
          <search-bar-filter-tag type="establishments"/>
        </div>

        <div class="Group Timing" :class="{'Selected': selected === 'timing'}">
          <h2 class="FilterName">Timing</h2>
          <search-bar-filter-timing/>
        </div>

        <div class="BottomBar">
          <div class="Button Cancel" @click="onCancel">Cancel</div>
          <div class="Button Apply Primary500Bg White" @click="onApply">Apply</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import SearchBarFilterTag from "./SearchBarFilterTag";
  import SearchBarFilterTiming from "./SearchBarFilterTiming";

  export default {
    name: "SearchBarFilterList",
    components: {SearchBarFilterTiming, SearchBarFilterTag},
    props: {
      selected: {
        required: true
      }
    },
    methods: {
      onCancel() {
        this.$emit('selected', this.selected)
      },
      onApply() {
        this.$emit('selected', this.selected)
      },
    }
  }
</script>

<style scoped lang="less">
  .FilterListContainer {
  }

  .FilterList {
    z-index: 800;
  }

  .FilterName {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    margin-top: 24px;
    margin-bottom: 8px;
  }

  .BottomBar {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;

    .Button {
      line-height: 1.5;
      border-radius: 3px;

      &:hover {
        cursor: pointer;
      }
    }
  }

  @media (max-width: 767.98px) {
    .FilterListContainer {
      z-index: 100;
      background: white;
    }

    .FilterList {
      @media (min-width: 576px) {
        max-width: 540px;
        margin-right: auto;
        margin-left: auto;
      }

      padding-bottom: 8px;
    }

    .BottomBar {
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;

      background: white;
      box-shadow: 0 -1px 2px 0 rgba(0, 0, 0, 0.2), 0 -1px 3px 0 rgba(0, 0, 0, 0.1);

      .Cancel {
        display: none;
      }

      .Apply {
        font-weight: 600;
        font-size: 16px;
        height: 44px;
        line-height: 44px;
        text-align: center;

        margin: 10px 15px;
        flex-grow: 1;
      }
    }
  }

  @media (min-width: 768px) {
    .FilterListContainer {
      z-index: 1000;
      position: absolute;
    }

    .FilterList {
      padding: 16px 24px;
      border-radius: 0 0 4px 4px;
      box-shadow: 0 3px 6px 0 rgba(0, 0, 0, 0.23), 0 3px 6px 0 rgba(0, 0, 0, 0.16);

      background: white;
      margin-top: 1px;
    }

    .FilterName {
      display: none;
    }

    .Group {
      display: none;
    }

    .Group.Selected {
      display: block;
    }

    .BottomBar {
      .Button {
        padding: 6px 24px;
        margin-left: 8px;
      }
    }
  }

</style>
