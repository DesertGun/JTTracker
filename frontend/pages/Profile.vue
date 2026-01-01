<template>
  <div class="profileMain">
    <b-container class="profileHeaderContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Profile</h3>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container class="profileContentContainer" fluid>
      <b-row>
        <b-col />
        <b-col style="min-width: fit-content">
          <b-card>
            <b-card-body>
              <b-form>
                <b-row>
                  <b-col>
                    <b-form-group
                      id="usernameFormGroup"
                      description="Unique username for yor account"
                      label="Username"
                      label-for="username"
                    >
                      <b-form-input
                        id="username"
                        v-model="username"
                        disabled="true"
                        type="text"
                      />
                    </b-form-group>
                  </b-col>
                  <b-col>
                    <b-form-group
                      id="accountNameFormGroup"
                      description="Name linked to your account"
                      label="Account name"
                      label-for="accountName"
                    >
                      <b-form-input
                        id="accountName"
                        v-model="accountName"
                        placeholder="None"
                        type="text"
                      />
                    </b-form-group>
                  </b-col>
                </b-row>
                <b-row>
                  <b-col />
                  <b-col>
                    <b-form-invalid-feedback
                      :state="validationaccountNameChange"
                    >
                      Your global name cannot be empty!
                    </b-form-invalid-feedback>
                    <b-form-invalid-feedback :state="validationaccountNameNew">
                      No changes detected !
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback
                      :state="validationaccountNameChange"
                    />
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col>
                    <div
                      v-if="
                        validationaccountNameChange &&
                        accountName !== username &&
                        validationaccountNameNew
                      "
                      class="pb-2"
                    >
                      <b-button
                        class="w-100"
                        variant="success"
                        @click="updateProfile"
                      >
                        Update profile
                      </b-button>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col style="min-width: fit-content; max-width: min-content">
                    <div v-if="updated" class="mt-2" style="text-align: center">
                      <b-alert dismissible show variant="success">
                        Your profile was updated successfully!
                      </b-alert>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col class="avatar-content">
                    <b-form-group
                      id="avatarFormGroup"
                      label="Avatar"
                      label-for="avatar"
                    >
                      <div v-if="!hasProfilePicture">
                        <b-img-lazy
                          :src="
                            'https://gravatar.com/avatar/' +
                            getHash +
                            '?d=identicon'
                          "
                          height="100px"
                          width="100px"
                          rounded="circle"
                        />
                      </div>
                      <div v-else>
                        <b-img-lazy
                          :src="getProfilePicture"
                          height="100px"
                          width="100px"
                          rounded="circle"
                        />
                      </div>
                    </b-form-group>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col style="min-width: fit-content">
                    <b-form-file
                      v-model="profilePictureUpload"
                      class="mb-2"
                      accept=".jpg"
                    ></b-form-file>
                    <div v-if="uploadCurrent > 0">
                      <b-progress
                        :value="uploadCurrent"
                        :max="uploadMax"
                        show-progress
                        animated
                      ></b-progress>
                    </div>
                    <b-row>
                      <b-col />
                      <b-col style="min-width: fit-content">
                        <div v-if="profilePictureUpload">
                          <b-button
                            variant="primary"
                            class="mt-2"
                            @click="uploadProfilePicture"
                          >
                            Upload my profile picture !
                          </b-button>
                        </div>
                        <div v-if="hasProfilePicture">
                          <b-button
                            variant="outline-danger"
                            class="mt-2"
                            @click="deleteProfilePicture"
                          >
                            Delete my profile picture !
                          </b-button>
                        </div>
                      </b-col>
                      <b-col />
                    </b-row>
                    <b-row>
                      <b-col />
                      <b-col
                        style="min-width: fit-content; max-width: min-content"
                      >
                        <div
                          v-if="responseSuccess"
                          class="pt-2"
                          style="text-align: center"
                        >
                          <b-alert dismissible show variant="success">
                            {{ responseSuccess }}
                          </b-alert>
                        </div>
                        <div
                          v-if="responseError"
                          class="pt-2"
                          style="text-align: center"
                        >
                          <b-alert dismissible show variant="danger">
                            {{ responseError }}
                          </b-alert>
                        </div>
                      </b-col>
                      <b-col />
                    </b-row>
                  </b-col>
                  <b-col />
                </b-row>
                <b-row class="mt-2" align-h="between">
                  <b-col cols="4" />
                  <b-col
                    cols="4"
                    style="min-width: fit-content; text-align: end"
                  >
                    <b-button variant="outline-danger" @click="deleteAccount()">
                      <b-icon icon="trash-fill" />
                    </b-button>
                  </b-col>
                </b-row>
              </b-form>
            </b-card-body>
          </b-card>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'ProfilePage',
  data() {
    return {
      username: null,
      accountName: '',
      initaccountName: '',
      changed: false,
      updated: false,
      profilePictureUpload: null,
      responseError: null,
      responseSuccess: null,
      uploadCurrent: 0,
      uploadMax: 100,
    }
  },
  computed: {
    validationaccountNameChange() {
      return this.accountName.length > 0
    },
    validationaccountNameNew() {
      return this.accountName !== this.initaccountName
    },
    ...mapGetters({
      getProfilePicture: 'user.store/getProfilePicture',
      getHash: 'user.store/getHash',
      hasProfilePicture: 'user.store/hasProfilePicture',
      getUsername: 'user.store/getUsername',
      getAccountname: 'user.store/getAccountname',
    }),
  },
  mounted() {
    try {
      this.username = this.getUsername
      if (this.getAccountname) {
        this.accountName = this.getAccountname
        this.initaccountName = this.accountName
      } else {
        this.accountName = this.username
        this.initaccountName = this.accountName
      }
      this.hash = this.getHash
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    deleteAccount() {
      this.$router.push('/accountDelete')
    },
    async updateProfile() {
      try {
        const response = await this.$axios.put('/user/update', {
          username: this.username,
          accountName: this.accountName,
        })
        if (response.data.validated === true) {
          this.updated = true
          this.accountName = this.getAccountname
          this.initaccountName = this.accountName
        }
        await this.$store.dispatch('user.store/setProfileData')
      } catch (e) {
        alert(e.toString())
      }
    },
    async uploadProfilePicture() {
      try {
        const formData = new FormData()
        formData.append('profilePicture', this.profilePictureUpload)

        const response = await this.$axios.post('/user/picture', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          onUploadProgress: function (progressEvent) {
            this.uploadCurrent = parseInt(
              Math.round((progressEvent.loaded / progressEvent.total) * 100)
            )
          }.bind(this),
        })
        this.profilePictureUpload = null
        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
          await this.$store.dispatch('user.store/setProfilePicture')
          this.uploadCurrent = 0
          this.profilePicture = this.getProfilePicture
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    async deleteProfilePicture() {
      try {
        const response = await this.$axios.delete('/user/picture')
        if (response.data.successMessage) {
          this.responseSuccess = response.data.successMessage
          this.$store.dispatch('user.store/deleteProfilePicture')
        }
      } catch (e) {
        alert(e.toString())
      }
    },
  },
}
</script>

<style>
.avatar-content {
  text-align: center;
}
</style>
