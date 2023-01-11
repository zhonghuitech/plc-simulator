<script lang="tsx">
import type { PropType } from 'vue';
import { Result, Button } from 'ant-design-vue';
import { defineComponent, unref } from 'vue';

export default defineComponent({
  name: 'ErrorPage',
  props: {
    title: {
      type: String as PropType<string>,
      default: '',
    },    
  },
  setup(props) {
    
    return () => {
      const title = "Good" || {};
      return (
        <Result  title={props.title || title} >
          {{
          }}
        </Result>
      );
    };
  },
});
</script>
<style lang="less">
</style>
