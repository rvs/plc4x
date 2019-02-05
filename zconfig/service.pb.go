// Code generated by protoc-gen-go. DO NOT EDIT.
// source: service.proto

package zconfig

import proto "github.com/golang/protobuf/proto"
import fmt "fmt"
import math "math"

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

type ZSrvType int32

const (
	ZSrvType_ZsrvFirst      ZSrvType = 0
	ZSrvType_ZsrvStrongSwan ZSrvType = 1
	ZSrvType_ZsrvLISP       ZSrvType = 2
	ZSrvType_ZsrvBridge     ZSrvType = 3
	ZSrvType_ZsrvNAT        ZSrvType = 4
	ZSrvType_ZsrvLB         ZSrvType = 5
	ZSrvType_ZsrvLast       ZSrvType = 255
)

var ZSrvType_name = map[int32]string{
	0:   "ZsrvFirst",
	1:   "ZsrvStrongSwan",
	2:   "ZsrvLISP",
	3:   "ZsrvBridge",
	4:   "ZsrvNAT",
	5:   "ZsrvLB",
	255: "ZsrvLast",
}
var ZSrvType_value = map[string]int32{
	"ZsrvFirst":      0,
	"ZsrvStrongSwan": 1,
	"ZsrvLISP":       2,
	"ZsrvBridge":     3,
	"ZsrvNAT":        4,
	"ZsrvLB":         5,
	"ZsrvLast":       255,
}

func (x ZSrvType) String() string {
	return proto.EnumName(ZSrvType_name, int32(x))
}
func (ZSrvType) EnumDescriptor() ([]byte, []int) { return fileDescriptor8, []int{0} }

// Service Opaque config. In future we might add more fields here
// but idea is here. This is service specific configuration.
type ServiceOpaqueConfig struct {
	Oconfig string `protobuf:"bytes,1,opt,name=oconfig" json:"oconfig,omitempty"`
}

func (m *ServiceOpaqueConfig) Reset()                    { *m = ServiceOpaqueConfig{} }
func (m *ServiceOpaqueConfig) String() string            { return proto.CompactTextString(m) }
func (*ServiceOpaqueConfig) ProtoMessage()               {}
func (*ServiceOpaqueConfig) Descriptor() ([]byte, []int) { return fileDescriptor8, []int{0} }

func (m *ServiceOpaqueConfig) GetOconfig() string {
	if m != nil {
		return m.Oconfig
	}
	return ""
}

// Service Lisp config
type ServiceLispConfig struct {
	LispMSs             []*ZcServicePoint `protobuf:"bytes,1,rep,name=LispMSs" json:"LispMSs,omitempty"`
	LispInstanceId      uint32            `protobuf:"varint,2,opt,name=LispInstanceId" json:"LispInstanceId,omitempty"`
	Allocate            bool              `protobuf:"varint,3,opt,name=allocate" json:"allocate,omitempty"`
	Exportprivate       bool              `protobuf:"varint,4,opt,name=exportprivate" json:"exportprivate,omitempty"`
	Allocationprefix    []byte            `protobuf:"bytes,5,opt,name=allocationprefix,proto3" json:"allocationprefix,omitempty"`
	Allocationprefixlen uint32            `protobuf:"varint,6,opt,name=allocationprefixlen" json:"allocationprefixlen,omitempty"`
	Experimental        bool              `protobuf:"varint,20,opt,name=Experimental" json:"Experimental,omitempty"`
}

func (m *ServiceLispConfig) Reset()                    { *m = ServiceLispConfig{} }
func (m *ServiceLispConfig) String() string            { return proto.CompactTextString(m) }
func (*ServiceLispConfig) ProtoMessage()               {}
func (*ServiceLispConfig) Descriptor() ([]byte, []int) { return fileDescriptor8, []int{1} }

func (m *ServiceLispConfig) GetLispMSs() []*ZcServicePoint {
	if m != nil {
		return m.LispMSs
	}
	return nil
}

func (m *ServiceLispConfig) GetLispInstanceId() uint32 {
	if m != nil {
		return m.LispInstanceId
	}
	return 0
}

func (m *ServiceLispConfig) GetAllocate() bool {
	if m != nil {
		return m.Allocate
	}
	return false
}

func (m *ServiceLispConfig) GetExportprivate() bool {
	if m != nil {
		return m.Exportprivate
	}
	return false
}

func (m *ServiceLispConfig) GetAllocationprefix() []byte {
	if m != nil {
		return m.Allocationprefix
	}
	return nil
}

func (m *ServiceLispConfig) GetAllocationprefixlen() uint32 {
	if m != nil {
		return m.Allocationprefixlen
	}
	return 0
}

func (m *ServiceLispConfig) GetExperimental() bool {
	if m != nil {
		return m.Experimental
	}
	return false
}

type ServiceInstanceConfig struct {
	Id          string   `protobuf:"bytes,1,opt,name=id" json:"id,omitempty"`
	Displayname string   `protobuf:"bytes,2,opt,name=displayname" json:"displayname,omitempty"`
	Srvtype     ZSrvType `protobuf:"varint,3,opt,name=srvtype,enum=ZSrvType" json:"srvtype,omitempty"`
	// Optional in future we might need this
	// 	VmConfig fixedresources = 3;
	// 	repeated Drive drives = 4;
	Activate bool `protobuf:"varint,5,opt,name=activate" json:"activate,omitempty"`
	// towards application which networkUUID to use
	// FIXME: In future there might multiple application network assignment
	// so this will become repeated.
	Applink string `protobuf:"bytes,10,opt,name=applink" json:"applink,omitempty"`
	// towards devices which phyiscal or logical adapter to use
	Devlink *Adapter `protobuf:"bytes,20,opt,name=devlink" json:"devlink,omitempty"`
	// Opaque configuration
	Cfg     *ServiceOpaqueConfig `protobuf:"bytes,30,opt,name=cfg" json:"cfg,omitempty"`
	LispCfg *ServiceLispConfig   `protobuf:"bytes,31,opt,name=lispCfg" json:"lispCfg,omitempty"`
}

func (m *ServiceInstanceConfig) Reset()                    { *m = ServiceInstanceConfig{} }
func (m *ServiceInstanceConfig) String() string            { return proto.CompactTextString(m) }
func (*ServiceInstanceConfig) ProtoMessage()               {}
func (*ServiceInstanceConfig) Descriptor() ([]byte, []int) { return fileDescriptor8, []int{2} }

func (m *ServiceInstanceConfig) GetId() string {
	if m != nil {
		return m.Id
	}
	return ""
}

func (m *ServiceInstanceConfig) GetDisplayname() string {
	if m != nil {
		return m.Displayname
	}
	return ""
}

func (m *ServiceInstanceConfig) GetSrvtype() ZSrvType {
	if m != nil {
		return m.Srvtype
	}
	return ZSrvType_ZsrvFirst
}

func (m *ServiceInstanceConfig) GetActivate() bool {
	if m != nil {
		return m.Activate
	}
	return false
}

func (m *ServiceInstanceConfig) GetApplink() string {
	if m != nil {
		return m.Applink
	}
	return ""
}

func (m *ServiceInstanceConfig) GetDevlink() *Adapter {
	if m != nil {
		return m.Devlink
	}
	return nil
}

func (m *ServiceInstanceConfig) GetCfg() *ServiceOpaqueConfig {
	if m != nil {
		return m.Cfg
	}
	return nil
}

func (m *ServiceInstanceConfig) GetLispCfg() *ServiceLispConfig {
	if m != nil {
		return m.LispCfg
	}
	return nil
}

func init() {
	proto.RegisterType((*ServiceOpaqueConfig)(nil), "ServiceOpaqueConfig")
	proto.RegisterType((*ServiceLispConfig)(nil), "ServiceLispConfig")
	proto.RegisterType((*ServiceInstanceConfig)(nil), "ServiceInstanceConfig")
	proto.RegisterEnum("ZSrvType", ZSrvType_name, ZSrvType_value)
}

func init() { proto.RegisterFile("service.proto", fileDescriptor8) }

var fileDescriptor8 = []byte{
	// 509 bytes of a gzipped FileDescriptorProto
	0x1f, 0x8b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0xff, 0x6c, 0x93, 0xdf, 0x6e, 0xd3, 0x30,
	0x18, 0xc5, 0x49, 0xba, 0x35, 0xed, 0xd7, 0x3f, 0x0b, 0xde, 0x90, 0xa2, 0x49, 0xb0, 0xa8, 0xa0,
	0xa9, 0x4c, 0x28, 0x45, 0xe5, 0x05, 0x58, 0x11, 0x48, 0x95, 0x06, 0x4c, 0xc9, 0xae, 0x7a, 0xe7,
	0x25, 0x6e, 0xb0, 0x48, 0x6c, 0xe3, 0xb8, 0xa1, 0xdd, 0xc3, 0xf0, 0x00, 0xbc, 0x24, 0xc8, 0x8e,
	0x83, 0xe8, 0xb6, 0x3b, 0x9f, 0x73, 0x7e, 0x96, 0xec, 0xe3, 0xcf, 0x30, 0xaa, 0x88, 0xac, 0x69,
	0x4a, 0x22, 0x21, 0xb9, 0xe2, 0xa7, 0x47, 0x19, 0xa9, 0x53, 0x5e, 0x96, 0x9c, 0x35, 0xc6, 0x64,
	0x06, 0xc7, 0x49, 0x43, 0x7c, 0x15, 0xf8, 0xc7, 0x86, 0x7c, 0xe0, 0x6c, 0x4d, 0x73, 0x14, 0x80,
	0xc7, 0x53, 0xb3, 0x0c, 0x9c, 0xd0, 0x99, 0xf6, 0xe3, 0x56, 0x4e, 0x7e, 0xbb, 0xf0, 0xd4, 0xee,
	0xb8, 0xa2, 0x95, 0xb0, 0xfc, 0x6b, 0xf0, 0xb4, 0xfa, 0x9c, 0x54, 0x81, 0x13, 0x76, 0xa6, 0x83,
	0xf9, 0x51, 0xb4, 0x4a, 0x2d, 0x76, 0xcd, 0x29, 0x53, 0x71, 0x9b, 0xa3, 0x73, 0x18, 0xeb, 0xe5,
	0x92, 0x55, 0x0a, 0xb3, 0x94, 0x2c, 0xb3, 0xc0, 0x0d, 0x9d, 0xe9, 0x28, 0xbe, 0xe7, 0xa2, 0x53,
	0xe8, 0xe1, 0xa2, 0xe0, 0x29, 0x56, 0x24, 0xe8, 0x84, 0xce, 0xb4, 0x17, 0xff, 0xd3, 0xe8, 0x15,
	0x8c, 0xc8, 0x56, 0x70, 0xa9, 0x84, 0xa4, 0xb5, 0x06, 0x0e, 0x0c, 0xb0, 0x6f, 0xa2, 0x0b, 0xf0,
	0xed, 0x0e, 0xca, 0x99, 0x90, 0x64, 0x4d, 0xb7, 0xc1, 0x61, 0xe8, 0x4c, 0x87, 0xf1, 0x03, 0x1f,
	0xbd, 0x85, 0xe3, 0xfb, 0x5e, 0x41, 0x58, 0xd0, 0x35, 0x47, 0x7b, 0x2c, 0x42, 0x13, 0x18, 0x7e,
	0xdc, 0x0a, 0x22, 0x69, 0x49, 0x98, 0xc2, 0x45, 0x70, 0x62, 0x8e, 0xb0, 0xe7, 0x4d, 0x7e, 0xb9,
	0xf0, 0xcc, 0xb6, 0xd0, 0xde, 0xcc, 0x16, 0x36, 0x06, 0x97, 0x66, 0xb6, 0x5b, 0x97, 0x66, 0x28,
	0x84, 0x41, 0x46, 0x2b, 0x51, 0xe0, 0x1d, 0xc3, 0x25, 0x31, 0x95, 0xf4, 0xe3, 0xff, 0x2d, 0xf4,
	0x12, 0xbc, 0x4a, 0xd6, 0x6a, 0x27, 0x9a, 0x3a, 0xc6, 0xf3, 0x7e, 0xb4, 0x4a, 0x64, 0x7d, 0xb3,
	0x13, 0x24, 0x6e, 0x13, 0x53, 0x5a, 0xaa, 0x9a, 0x4e, 0x0e, 0x6d, 0x69, 0x56, 0xeb, 0x37, 0xc5,
	0x42, 0x14, 0x94, 0x7d, 0x0f, 0xa0, 0x79, 0x53, 0x2b, 0xd1, 0x04, 0xbc, 0x8c, 0xd4, 0x26, 0xd1,
	0xb7, 0x18, 0xcc, 0x7b, 0xd1, 0x65, 0x86, 0x85, 0x22, 0x32, 0x6e, 0x03, 0x74, 0x0e, 0x9d, 0x74,
	0x9d, 0x07, 0x2f, 0x4c, 0x7e, 0x12, 0x3d, 0x32, 0x34, 0xb1, 0x06, 0xd0, 0x1b, 0xf0, 0x0a, 0x3d,
	0x17, 0xeb, 0x3c, 0x38, 0x33, 0x2c, 0x8a, 0x1e, 0x8c, 0x4b, 0xdc, 0x22, 0x17, 0x15, 0xf4, 0xda,
	0x4b, 0xa0, 0x11, 0xf4, 0x57, 0x95, 0xac, 0x3f, 0x51, 0x59, 0x29, 0xff, 0x09, 0x42, 0x30, 0xd6,
	0x32, 0x51, 0x92, 0xb3, 0x3c, 0xf9, 0x89, 0x99, 0xef, 0xa0, 0x21, 0xf4, 0xb4, 0x77, 0xb5, 0x4c,
	0xae, 0x7d, 0x17, 0x8d, 0x01, 0xb4, 0x5a, 0x48, 0x9a, 0xe5, 0xc4, 0xef, 0xa0, 0x01, 0x78, 0x5a,
	0x7f, 0xb9, 0xbc, 0xf1, 0x0f, 0x10, 0x40, 0xd7, 0xa0, 0x0b, 0xff, 0x10, 0x8d, 0xec, 0x36, 0x5c,
	0x29, 0xff, 0x8f, 0xb3, 0x78, 0x0f, 0x67, 0x29, 0x2f, 0xa3, 0x3b, 0x92, 0x91, 0x0c, 0x47, 0x69,
	0xc1, 0x37, 0x59, 0xb4, 0xd9, 0xfb, 0x27, 0xab, 0xe7, 0x39, 0x55, 0xdf, 0x36, 0xb7, 0x51, 0xca,
	0xcb, 0x59, 0xc3, 0xcd, 0xb0, 0xa0, 0xb3, 0xbb, 0xe6, 0x13, 0xdc, 0x76, 0x0d, 0xf5, 0xee, 0x6f,
	0x00, 0x00, 0x00, 0xff, 0xff, 0x77, 0xf4, 0xbe, 0x51, 0x5e, 0x03, 0x00, 0x00,
}
