KBRANCH ?= "v6.1/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.1/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.1/standard/qemuarm64"
KBRANCH:qemumips ?= "v6.1/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.1/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.1/standard/base"
KBRANCH:qemuriscv32  ?= "v6.1/standard/base"
KBRANCH:qemux86  ?= "v6.1/standard/base"
KBRANCH:qemux86-64 ?= "v6.1/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.1/standard/base"
KBRANCH:qemumips64 ?= "v6.1/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "96685a4bde4e1642af2413a7b78ee5ea4d544e81"
SRCREV_machine:qemuarm64 ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_machine:qemuloongarch64 ?= "1caf99d56881e1510afca553cd4a057f80479973"
SRCREV_machine:qemumips ?= "9ecebe9ac613fb9347e0fc0d932432571bc6c951"
SRCREV_machine:qemuppc ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_machine:qemuriscv64 ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_machine:qemuriscv32 ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_machine:qemux86 ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_machine:qemux86-64 ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_machine:qemumips64 ?= "bfd40de0f8fa3500b51b32e55be0a01723ee31e5"
SRCREV_machine ?= "d855e638945ed4223775c19f4eb203381c3bc24c"
SRCREV_meta ?= "6c723fc4d35fbdb277e28d0072f8117e43679528"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "129c15b606278f9254a16013f7e5a94a128d9bcd"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.1/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.1;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.1.12"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native libmpc-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32|qemuloongarch64)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

