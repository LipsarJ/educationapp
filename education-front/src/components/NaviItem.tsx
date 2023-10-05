import React from 'react'
import {Flex, Icon, Link, Menu, MenuButton, Text} from '@chakra-ui/react'
import {NavLink, useNavigate} from 'react-router-dom';

export default function NavItem({
                                    icon,
                                    title,
                                    description,
                                    url
                                }: { icon: any, title: string, description: string, url: string }) {
    const navigate = useNavigate();
    return (
        <Flex
            mt={30}
            flexDir="column"
            w="100%"
            alignItems="center"
        >
            <Menu placement="right">
                <Link
                    borderRadius = "8"
                    p={3}
                    color="#3D3D3D"
                    _hover={{textDecor: 'none', backgroundColor: "blue.500", color:'white'}}
                    w="100%"
                    as={NavLink} to={url}
                >
                    <MenuButton w="100%">
                        <Flex flexDir = "row" textAlign = "left">
                            <Icon as={icon} fontSize="xl"/>
                            <Text ml={5} display="flex">{title}</Text>
                        </Flex>
                    </MenuButton>
                </Link>
            </Menu>
        </Flex>
    )
}