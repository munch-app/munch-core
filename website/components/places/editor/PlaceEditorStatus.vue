<template>
  <div class="border border-3 bg-steam p-16">
    <div class="flex-wrap m--6">
      <div class="p-6" v-for="type in types" :key="type.type">
        <div class="p-4-8 border-2 small-bold hover-pointer"
             :class="{
             'bg-blue white': type.type === editing.type,
             'bg-white black': type.type !== editing.type
             }"
             @click="onClick(type)">
          {{type.name}}
        </div>
      </div>
    </div>
    <div v-if="isActive('at')">
      <h6>At: (Date in Milliseconds)</h6>
      <div class="flex-align-center">
        <div class="flex-grow">
          <input v-model="editing.at">
        </div>
        <div class="ml-16 flex-self-stretch">
          <div
            @click="onSetToday"
            class="bg-blue border-2 h-100 flex-center hover-pointer">
            <div class="mlr-16 white border-2 small-bold">Set Today</div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isActive('id')">
      <h6>Id: (Redirect To Place)</h6>
      <input v-model="editing.id">
    </div>

    <div v-if="isActive('reason')">
      <h6>Reason:</h6>
      <input v-model="editing.reason">
    </div>
  </div>
</template>

<script>
  export default {
    name: "PlaceEditorStatus",
    props: {
      value: {
        type: Object,
        default() {
          return {type: 'OPEN'}
        }
      },
    },
    data() {
      return {
        types: [
          {type: 'OPEN', name: 'Open'},
          {type: 'DORMANT', name: 'Dormant', at: true},
          {type: 'HIDDEN', name: 'Hidden'},
          {type: 'DELETED', name: 'Deleted', at: true, reason: true},
          {type: 'MOVED', name: 'Moved', at: true, id: true},
          {type: 'PERMANENTLY_CLOSED', name: 'Permanently Closed', at: true},
        ],
        editing: {
          at: null, id: null, reason: null,
          ...JSON.parse(JSON.stringify(this.value))
        }
      }
    },
    methods: {
      update() {
        this.$emit('input', this.editing)
      },
      onClick(type) {
        this.editing.type = type.type
      },
      isActive(field) {
        const config = this.types.filter(value => {
          return value.type === this.editing.type
        })

        if (config && config[0]) {
          return config[0][field]
        }
      },
      onSetToday() {
        this.editing.at = new Date().getTime()
      }
    }
  }
</script>

<style scoped lang="less">
  input {
    outline: none;
    border: none;

    background: #FFF;
    color: black;

    width: 100%;
    font-size: 14px;
    padding: 8px;
    border-radius: 1px;
  }

  h6 {
    margin-top: 12px;
    margin-bottom: 4px;
  }
</style>
