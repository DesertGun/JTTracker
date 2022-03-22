<template>
  <div class="accountDeleteMain">
    <b-container class="accountDeleteContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Account Deletion</h3>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container class="accountDeleteContentContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <b-card>
            <b-card-body>
              <b-row>
                <b-col />
                <b-col style="min-width:fit-content;text-align:center">
                  <p>Are you sure you want to delete the account?</p>
          <p>This is a final decision and cannot be reversed!</p>
          <p>
            If you decide to use the app again, a new registration must be made!
          </p>
                </b-col>
                <b-col />
              </b-row>
              <b-row>
                <b-col />
                <b-col style="min-width: fit-content">
                  <b-button variant="danger" @click="deleteAccountPerminantly">
            Yes please delete my account !
          </b-button>
                </b-col>
                <b-col />
              </b-row>
              <b-row>
                <b-col />
                <b-col style="min-width: fit-content;max-width: min-content;">
                  <div v-if="responseSuccess" class="pt-2" style="text-align: center">
            <b-alert dismissible show variant="success">
              {{ responseSuccess }}
            </b-alert>
          </div>
          <div v-if="responseError" class="pt-2" style="text-align: center">
            <b-alert dismissible show variant="danger">
              {{ responseError }}
            </b-alert>
          </div>
                </b-col>
                <b-col />
              </b-row>
            </b-card-body>
          </b-card>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'AccountDeletionPage',
  middleware: 'authenticated',
  asyncData() {
    return {
      username: '',
      responseSuccess: null,
      responseError: null,
    }
  },
  computed: {
    ...mapGetters({      getUsername: 'user/getUsername',
})
  },
  mounted() {
    this.username = this.getUsername
  },
  methods: {
    async deleteAccountPerminantly() {
      try {
        const response = await this.$axios.delete(
          'user/delete/' + this.username
        )
        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
          const delay = (ms) =>
            new Promise((resolve) => setTimeout(resolve, ms))
          await delay(10000)
          this.$store.dispatch('auth/logout')
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
  },
}
</script>

<style></style>
