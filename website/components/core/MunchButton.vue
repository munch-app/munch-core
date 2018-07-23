<template>
  <button class="Button Elevation1 Border48" :class="[variant.toLowerCase(), size.toLowerCase()]" @click="$emit('click')">
    <div class="Body">
      <div class="Text">{{text}}</div>
      <simple-svg v-if="icon" class="Icon" :fill="fillColor" :filepath="icon"/>
    </div>
  </button>
</template>

<script>
  export default {
    name: "MunchButton",
    props: {
      text: {
        required: true,
        type: String
      },
      icon: {
        required: false,
        type: String
      },
      variant: {
        required: false,
        type: String,
        default() {
          return 'primary'
        }
      },
      size: {
        required: false,
        type: String,
        default() {
          return 'regular'
        }
      }
    },
    computed: {
      fillColor() {
        switch (this.variant) {
          default:
          case 'primary':
            return 'white'
          case 'clear':
          case 'secondary':
          case 'tag':
            return 'black'
        }
      }
    }
  }
</script>

<style scoped lang="less">
  .Button {
    border: 0;

    &:hover {
      cursor: pointer;
    }

    .Text {
      margin: 0;
      float: left;
    }

    .Icon {
      padding: 0;
      margin: 0 0 0 6px;
      float: left;

      width: 22px;
      height: 22px;
    }
  }

  .Button {
    &.primary {
      background-color: #F15213;
      color: white
    }

    &.secondary {
      background-color: #718094;
      color: black
    }

    &.tag {
      background-color: #F0F0F7;
      color: black;
    }

    &.clear {
      background: white;
      color: black;
    }

    &.small {
      font-size: 12px;
      padding: 6px 16px;
    }

    &.medium {
      font-size: 14px;
      padding: 6px 12px;
    }

    &.regular {
      font-size: 16px;
      padding: 6px 14px;
    }

    &.large {
      font-size: 19px;
      padding: 8px 18px;
    }
  }
</style>
